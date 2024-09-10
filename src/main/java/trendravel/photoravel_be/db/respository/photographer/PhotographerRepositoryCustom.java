package trendravel.photoravel_be.db.respository.photographer;

import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.List;

public interface PhotographerRepositoryCustom {
    
    List<RecentReviewsDto> recentReviews(Long PhotographerId);
    
}
