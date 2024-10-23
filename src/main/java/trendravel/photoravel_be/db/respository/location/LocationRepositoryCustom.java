package trendravel.photoravel_be.db.respository.location;

import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.domain.location.dto.request.LocationKeywordDto;
import trendravel.photoravel_be.domain.location.dto.request.LocationNowPositionDto;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.List;

public interface LocationRepositoryCustom {

    List<RecentReviewsDto> recentReviews(Long id);
    List<Location> searchNowPosition(LocationNowPositionDto locationNowPositionDto);
    List<Location> searchKeyword(LocationKeywordDto locationKeywordDto);
    void increaseViews(Long id);

}
