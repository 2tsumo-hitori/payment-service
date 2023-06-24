package payment.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment.example.common.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
