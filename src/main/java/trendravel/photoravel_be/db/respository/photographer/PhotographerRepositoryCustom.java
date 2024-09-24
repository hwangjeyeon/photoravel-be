package trendravel.photoravel_be.db.respository.photographer;

import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.guidebook.Guidebook;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerListResponseDto;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.List;

public interface PhotographerRepositoryCustom {
    
    List<RecentReviewsDto> recentReviews(Long PhotographerId);
    
    List<Photographer> getPhotographerByCareer();
    
    List<Photographer> getPhotographerByMatchingCount();
    
    List<Photographer> getPhotographerByRegion(Region region);
}
