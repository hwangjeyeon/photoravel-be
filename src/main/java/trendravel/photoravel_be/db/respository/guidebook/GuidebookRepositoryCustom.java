package trendravel.photoravel_be.db.respository.guidebook;

import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.guidebook.Guidebook;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.domain.guidebook.dto.response.GuidebookListResponseDto;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.List;

public interface GuidebookRepositoryCustom {
    
    List<Guidebook> getGuidebookByViews();
    
    List<Guidebook> getGuidebookByNewest();
    
    List<Guidebook> getGuidebookByRegion(Region region);
}
