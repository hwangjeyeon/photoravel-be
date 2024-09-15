package trendravel.photoravel_be.db.respository.matching;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trendravel.photoravel_be.db.match.Matching;
import trendravel.photoravel_be.db.match.enums.MatchingStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    
    List<Matching> findByMemberId(String memberId);
    boolean existsByMemberIdAndStatusIn(String memberId, List<MatchingStatus> pending);
    Optional<Matching> findByMemberIdAndStatus(String memberId, MatchingStatus matchingStatus);
    
    
    
}
