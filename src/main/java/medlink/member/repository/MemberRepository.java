package medlink.member.repository;

import medlink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByLoginIdAndDeleteAtIsNull(String loginId);

    Optional<Member> findByLoginIdAndDeleteAtIsNull(String loginId);

    Optional<Member> findByUuid(String uuid);
}
