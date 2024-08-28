package trendravel.photoravel_be.db.respository.photographer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trendravel.photoravel_be.db.photographer.Photographer;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Long> {
    
    Optional<Photographer> findByAccountId(String accountId);
    
    Optional<List<Photographer>> findByNameContaining(String name);
    
    void deleteByAccountId(String accountId);
    
    //List<RecentReviewsDto> recentReviews(Long id);
    
}
