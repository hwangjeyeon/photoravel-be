package trendravel.photoravel_be.db.respository.location;

import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.List;

public interface LocationRepositoryCustom {

    List<RecentReviewsDto> recentReviews(Location location);

}
