package trendravel.photoravel_be.db.respository.spot;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.List;



import static trendravel.photoravel_be.db.review.QReview.review;
import static trendravel.photoravel_be.db.spot.QSpot.spot;


@RequiredArgsConstructor
public class SpotRepositoryImpl implements SpotRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RecentReviewsDto> recentReviews(Long locationId, Long spotId) {
        List<Review> recentReviews = queryFactory
                .select(review)
                .from(review)
                .leftJoin(spot).on(spot.location.id.eq(locationId))
                .leftJoin(review).on(review.spotReview.id.eq(spotId))
                .orderBy(review.updatedAt.desc())
                .limit(3)
                .fetch();

        return recentReviews.stream()
                .map(p -> new RecentReviewsDto(p.getContent(), p.getRating(), p.getImages()))
                .toList();
    }


}
