package payment.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment.example.domain.Member;

public interface memberRepository extends JpaRepository<Member, Long> {
}
