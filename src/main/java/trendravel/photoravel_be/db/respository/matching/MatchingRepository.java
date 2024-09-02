package trendravel.photoravel_be.db.respository.matching;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trendravel.photoravel_be.db.match.Matching;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    
}
