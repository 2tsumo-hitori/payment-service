# payment-service

## Subject : 결제모듈 활성화
- Language : Java 17
- FrameWork : Spring-boot, Spring Data Jpa, WebFlux
- Library : PortOne

## 주요 서비스
- 트랜잭션 전파 기능을 사용해 결제 시 4가지의 검증로직을 거쳐, 하나라도 예외가 생기면 rollback 처리
- commit -> 주문 생성
- rollback -> 서버에서 PortOne Api를 호출해 결제 취소
- Advice 처리 후 응답 결과 생성

## 외부 서비스 테스트 코드 작성
- 결제 서비스는 완벽한 검증을 필요로하기에 Mock을 사용하지않음
- Api 테스트는 Postman 사용

## 계층별 테스트 코드
![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/b3a8973d-32f2-4d39-bd8a-d7933138d8b6)

## Postman API 테스트
![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/0da72c09-5d6e-477c-a99b-585e01b3854a)

![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/1406f737-5192-491f-9a3e-681296e0cb3a)

## rollback시 결제 취소 처리
![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/7c28936c-5f11-42ff-b87b-c59330cebcaa)

![image](https://github.com/2tsumo-hitori/payment-service/assets/96719735/5e6cca6a-614a-44bd-b07c-8d492f3f19de)
