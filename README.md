# payment-service

## 사용 기술
- Java 17, Spring boot, Spring Data Jpa, Redis, Apache Kafka, Docker-compose

## 주요 서비스
- 상품 구매

## Trouble Shooting & Code Refactoring

- 
    <details>
      <summary>1차 FIX (탬플릿 콜백 패턴 활용 등 클래스별 결합도 ↓)</summary>
      
    ### 기존 코드
    
    ```java
    @Service
    @RequiredArgsConstructor
    public class PaymentAppService {
    
        private final IamportClient iamportClient;
    
        private final ItemRepository itemRepository;
    
        private final OrderService orderService;
    
        @Transactional
        public OrderResponse paymentValidate(PaymentRequest request) {
            Payment payment = null;
            
            try {
                payment = iamportClient.paymentByImpUid(request.getImpUid()).getResponse();
            } catch (IamportResponseException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    
            Optional<Item> item = itemRepository.findByName(request.getItemName());
    
            validation(payment, item);
    
            return orderService.makeOrder(item.get(), request.getMemberId());
        }
    
        private static void validation(Payment payment, Optional<Item> item) {
            itemExistValidate(item.isPresent());
            itemNameValidate(item.get().getName().equals(payment.getName()));
            itemPriceValidate(item.get().getPrice() == payment.getAmount().intValue());
        }
    }
    ```
    
    ### 기존 코드의 문제점
    
    - 해당 클래스에서 **외부 서비스를 의존**하면서 **동시에 비즈니스 로직을 처리**하고있음.
    - 강한 결합 등의 문제점 때문에 **유지보수성이 하락**함.
    
    ### 개선된 코드
    
    ```java
    @Service
    @RequiredArgsConstructor
    @Logger
    public class PurchaseService {
    
        private final ItemRepository itemRepository;
    
        private final PaymentTemplate paymentTemplate;
    
        private final AsyncOrderService asyncOrderService;
    
        @Transactional
        public String purchase(PaymentRequest request) {
            Item item = itemRepository.findByName(request.getItemName()).orElseThrow(ItemStatusException::new);
    
            itemPriceValidate(item.getQuantity() > request.getQuantity());
    
            return paymentTemplate.purchase(
                    new ValidatePayment(item, request.getImpUid(), request.getAmount()),
                    () -> asyncOrderService.decrease(item, request.getMemberId(), request.getQuantity()));
        }
    }
    ```
    
    ### 개선 이후의 코드
    
    - IamPortClient 라는 외부 라이브러리에 직접 의존하는 것이 아닌, **PaymentTemplate 인터페이스를 의존해 DIP와 OCP를 준수**함.
    - 탬플릿 콜백 패턴을 사용하여 **결제 검증 이후에 AsyncOrderService가 콜백으로 동작**함.
    </details>
- 
    <details>
      <summary>2차 FIX ( RDB 비관적 락 → Redis pub/sub 방식의 분산 락)</summary>
      
    ### 기존 코드
    
    ```java
    @Transactional
    public OrderResponse paymentValidate(PaymentRequest request) throws IamportResponseException, IOException {
        Payment payment = iamportClient.paymentByImpUid(request.getImpUid()).getResponse();
    
        Optional<Item> item = itemRepository.findByWithPessimisticLock(request.getItemName());
    
        validation(payment, item, request.getQuantity());
    
        OrderResponse response = orderService.purchase(item.get(), request.getMemberId(), request.getQuantity());
    
        return response;
    }
    
    ------------------------------------------------------------------------------------------------------------
    
    public interface ItemRepository extends JpaRepository<Item, Long> {
    
        @Lock(value = LockModeType.PESSIMISTIC_WRITE)
        @Query("select i from Item i where i.name = :name")
        Optional<Item> findByWithPessimisticLock(@Param("name") String name);
    }
    ```
    
    ### 기존 코드의 문제점
    
    - RDB의 데이터를 조회하기 위해 물리 디스크에 직접 접근함으로써, 원할한 서비스가 불가능함.
    - 만약 스케일 아웃으로  DB를 분산시킬 경우, 비관적 락으로는 동시성 이슈를 해결할 수 없음
    
    ### 개선된 코드
    
    ```java
    @RequiredArgsConstructor
    @Transactional
    @Service
    @Logger
    public class RedisAsyncOrderService implements AsyncOrderService {
    
        private final RedissonService redissonService;
    
        private final CreateOrderProducer createOrderProducer;
    
        private static final String PAYMENT_SUCCESS = "결제 완료";
    
        private static final String PAYMENT_FAILURE = "결제 실패";
    
        @Override
        public String decrease(Item item, Long memberId, Long quantity) {
            if (redissonService.isAccessLock(item.getStock().getId())) {
                item.getStock().decrease(quantity);
    
                createOrderProducer.create(memberId, item.getId());
    
                return PAYMENT_SUCCESS;
            }
    
            return PAYMENT_FAILURE;
        }
    }
    
    ------------------------------------------------------------------------------------
    
    @Component
    @RequiredArgsConstructor
    public class RedissonService {
    
        private final RedissonClient redissonClient;
    
        public boolean isAccessLock(Long key) {
            RLock lock = redissonClient.getLock(key.toString());
    
            try {
                return lock.tryLock(5, 1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }
    ```
    
    ### 개선된 코드
    
    - Redisson의 pub/sub 방식을 사용한 분산 락으로 동시성 이슈를 해결함
    - 해당 트랜잭션의 lock이 풀렸을 때 구독한 쓰레드들이 해당 트랜잭션에 접근함으로써, redis의 부하를 최소화 시킴.
    - 캐싱된 메모리에 접근하기 때문에 1000건 이상의 쓰레드가 동시에 접근하는 상황에서 **기존 로직보다 70% 이상 개선된 퍼포먼스**를 낼 수 있음.
</details>

- 
    <details>
      <summary>3차 FIX ( Message Broker를 사용해 멀티 모듈로 서비스 처리 및 서버 부담 분산 )</summary>
      
    ### 기존 코드
    
    ```java
    @Transactional
    public OrderResponse makeOrder(Item item, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
    
        itemStockValidate(item.getStock() > MINIMUM_SIZE);
    
        item.decreaseItemStock();
    
        Order order = orderRepository.save(Order
                .builder()
                .member(member)
                .item(item)
                .build());
    
        return orderRepository.findOrder(order.getId());
    }
    ```
    
    ### 기존 요청의 처리방식
    
    1. 상품 이름 조회
    2. PG 서버의 결제 검증
    3. RDB에서 상품과 RequestBody로 들어온 상품이 일치하는지 검증
    4. 재고 처리 및 주문 저장
    
    ### 한계점
    
    - **한 요청이 너무 많은 책임**을 가지고있어,
    특정 영역에서 사이드 이팩트가 발생할 시 해결이 어려움.
    - 한 요청을 처리하기 위해 **많은 시간이 소요됨 (약 600 ~ 700 ms )**
    
    ### 한계점을 타개하기 위한 아이디어
    
    1. **[❎](https://ko.emojiguide.com/%ea%b8%b0%ed%98%b8/cross-mark-button/)** PG 서버의 결제 검증이 끝나게되면 프론트 서버로 결과를 반환, 이후에 프론트 쪽에서 한번 더 요청을 보내 주문을 생성
        - 문제점 :
            - 여러번의 요청과 응답 중 네트워크 문제로 인해 PG 서버의 결제 검증 결과를 잃을 수 있음
            - 한두개 정도의 요청이야 괜찮겠지만, 만약 이 서비스가 엄청나게 성장해서 초당 1000건의 구매 요청이 들어오게 된다면 서버에 많은 부담이 생김
    2. **[❎](https://ko.emojiguide.com/%ea%b8%b0%ed%98%b8/cross-mark-button/)** WebClient나 RestTemplate을 사용해서 결제 검증 이후 로직을 비동기로 처리 
        - 문제점 :
            - 어차피 비동기 요청을 보내봤자 우리 서버로 보내게될텐데, 
            서버의 부담을 해결할 수 없음.
    3.  **[✅](https://ko.emojiguide.com/%ea%b8%b0%ed%98%b8/check-mark-button/) 멀티 모듈을 활용해 각각의 서비스를 처리해주는 서버들을 통해 해결** 
    - 애플리케이션 서버의 부담 ↓
    - 서비스 로직의 복잡함 해결
    - 공통 모듈을 정의함으로써 중복 코드 배제
    
    ### 한계 돌파
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/fa3cbc0c-70a1-4d8f-b329-2afa3266c2b6/Untitled.png)
    
    1. 확장 가능한 유연한 모듈 설계
    2. 메세지 브로커(Apache Kafka)를 사용해서 결제 검증 이후 토픽에 메세지 생성, 
    컨슈머에서 메세지를 받아 주문생성 로직 처리
    3. 상품 구매 이후 다른 이벤트를 처리하고 싶을 때 기존 코드 변경없이 해당 토픽을 구독하는 컨슈머를 하나 더 생성해서 확장할 수 있음. 
    (ex : 상품 구매 시 상품의 5% 금액을 적립하는 서비스 )
    
    ### 개선된 코드
    
    ```java
    @Override
    public String decrease(Item item, Long memberId, Long quantity) {
        if (redissonService.isAccessLock(item.getStock().getId())) {
            item.getStock().decrease(quantity);
    
            createOrderProducer.create(memberId, item.getId());
    
            return PAYMENT_SUCCESS;
        }
    
        return PAYMENT_FAILURE;
    }
    
    ---------------------------------------------------------------------------------
    
    @Component
    @RequiredArgsConstructor
    public class CreateOrderProducer {
        private final KafkaTemplate<String, OrderMessage> kafkaTemplate;
    
        public void create(Long userId, Long itemId) {
            kafkaTemplate.send("order_topic", new OrderMessage(userId, itemId));
        }
    }
    
    ---------------------------------------------------------------------------------
    
    @Component
    @RequiredArgsConstructor
    public class KafkaConsumerService implements MessageQueueService<OrderMessage> {
    
        private final OrderRepository orderRepository;
    
        private final MemberRepository memberRepository;
    
        private final ItemRepository itemRepository;
    
        @Override
        @KafkaListener(topics = "order_topic", groupId = "order_group")
        public void orderListener(OrderMessage listener) {
            Item item = itemRepository.findById(listener.getItemId()).orElseThrow();
            Member member = memberRepository.findById(listener.getMemberId()).orElseThrow();
    
            orderRepository.save(Order
                    .builder()
                    .member(member)
                    .item(item)
                    .build());
        }
    }
    ```
</details>

- 
    <details>
        <summary>4차 FIX ( 분산 락 AOP로 처리 )</summary>
      
    ### 기존 코드
    
    ```java
    @RequiredArgsConstructor
    @Transactional
    @Service
    @Logger
    public class RedisAsyncOrderService implements AsyncOrderService {
    
        private final RedissonService redissonService;
    
        private final CreateOrderProducer createOrderProducer;
    
        private static final String PAYMENT_SUCCESS = "결제 완료";
    
        private static final String PAYMENT_FAILURE = "결제 실패";
    
        @Override
        public String decrease(Item item, Long memberId, Long quantity) {
            if (redissonService.isAccessLock(item.getStock().getId())) {
                item.getStock().decrease(quantity);
    
                createOrderProducer.create(memberId, item.getId());
    
                return PAYMENT_SUCCESS;
            }
    
            return PAYMENT_FAILURE;
        }
    }
    ```
    
    ### 기존 코드의 문제점
    
    - if문은 항상 참인데, 거짓의 상황에서의 로직 또한 작성하고 있음.
    - 비즈니스 로직이 외부 서비스(RedissonService)에 의존하고 있음.
    - redis의 분산 락이 필요한 시점에 중복 코드가 발생함.
    
    ### 개선된 코드
    
    ```java
    @RequiredArgsConstructor
    @Transactional
    @Service
    @Logger
    public class DefaultAsyncOrderService implements AsyncOrderService {
    
        private final CreateOrderProducer createOrderProducer;
    
        private static final String PAYMENT_SUCCESS = "결제 완료";
    
        @Override
        **@AccessLock(key = "#item.getStockId().toString()", prefix = "item")**
        public String decrease(Item item, Long memberId, Long quantity) {
            item.getStock().decrease(quantity);
    
            createOrderProducer.create(memberId, item.getId());
    
            return PAYMENT_SUCCESS;
        }
    }
    
    ------------------------------------------------------------------------------
    
    @Aspect
    @Component
    @RequiredArgsConstructor
    @Slf4j
    public class AccessLockAspect {
    
        private final RedissonClient redissonClient;
    
        private static final String REDISSON_LOCK_PREFIX = "LOCK:";
    
        @Around("@annotation(accessLock))")
        public Object execute(ProceedingJoinPoint joinPoint, AccessLock accessLock) throws Throwable {
            String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
    
            String key = REDISSON_LOCK_PREFIX +
                    accessLock.prefix() +
                    CustomSpringElParser.getDynamicValue(parameterNames, joinPoint.getArgs(), accessLock.key());
    
            RLock lock = redissonClient.getLock(key);
    
            try {
                boolean access = lock.tryLock(accessLock.waitTime(), accessLock.leaseTime(), accessLock.timeUnit());
    
                if (!access)
                    return false;
    
                return joinPoint.proceed();
            } catch (InterruptedException e) {
                throw new InterruptedException();
            } finally {
                lock.unlock();
            }
        }
    }
    ```
    
    ### 개선된 코드
    
    - 어노테이션 포인트컷을 사용해 AccessLockAspect 어드바이스에서 분산 락을 적용함.
    - SPEL로 런타임 시점에 객체를 조작해 Lock의 key를 설정해줌.
    - 분산 락을 적용할 서비스에 어노테이션을 설정해주면 **중복 코드 방지 가능**
  </details>

## 1차 속도 개선 ( 9800ms → 2817ms )
![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/e295796b-6f88-4985-8f3c-dc9f941ee23d)
![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/b395eb45-49e5-4ce6-9c40-84d62f38775a)

## 2차 속도 개선 ( 651ms → 93ms )
![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/65cb3070-fa21-4696-b6c9-b7efe06ed719)
![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/5ced826c-fb69-4a2f-9bc2-e7cd97660da9)



## 계층별 테스트 코드
![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/b3a8973d-32f2-4d39-bd8a-d7933138d8b6)

## Postman API 테스트
![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/0da72c09-5d6e-477c-a99b-585e01b3854a)

![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/1406f737-5192-491f-9a3e-681296e0cb3a)

## 트랜잭션 롤백시 결제 취소 처리
![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/7c28936c-5f11-42ff-b87b-c59330cebcaa)

![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/5e6cca6a-614a-44bd-b07c-8d492f3f19de)
