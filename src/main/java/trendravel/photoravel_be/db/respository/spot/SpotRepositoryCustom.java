package trendravel.photoravel_be.db.respository.spot;

import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.List;

public interface SpotRepositoryCustom {

    List<RecentReviewsDto> recentReviews(Long SpotId);

}
