package trendravel.photoravel_be.db.respository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import trendravel.photoravel_be.db.member.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByEmail(String email);
    Optional<MemberEntity> findByEmail(String email);
    Optional<MemberEntity> findByMemberId(String memberId);
    Optional<MemberEntity> findByNickname(String nickname);
    
    boolean existsByMemberId(String memberId);
}


