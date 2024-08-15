package trendravel.photoravel_be.db.respository.guide;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trendravel.photoravel_be.db.guide.Guide;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    
    Optional<Guide> findByAccountId(String accountId);
    
    List<Guide> findByNameContaining(String name);
    
    void deleteByAccountId(String guideId);
    
    //List<RecentReviewsDto> recentReviews(Long id);
    
}
