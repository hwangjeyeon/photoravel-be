package trendravel.photoravel_be.db.respository.location;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;


import java.util.List;


import static trendravel.photoravel_be.db.location.QLocation.location;
import static trendravel.photoravel_be.db.review.QReview.review;

@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RecentReviewsDto> recentReviews() {
        List<Review> recentReviews = queryFactory
                .select(review)
                .from(review)
                .innerJoin(review.locationReview, location)
                .orderBy(review.updatedAt.desc())
                .limit(3)
                .fetch();

        return recentReviews.stream()
                .map(p -> new RecentReviewsDto(p.getContent(), p.getRating(), p.getImages()))
                .toList();
    }
}
