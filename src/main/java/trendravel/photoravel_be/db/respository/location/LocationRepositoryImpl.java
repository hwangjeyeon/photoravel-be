package trendravel.photoravel_be.db.respository.location;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.List;

import static trendravel.photoravel_be.db.review.QReview.review;

public class LocationRepositoryImpl implements LocationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public LocationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<RecentReviewsDto> recentReviews(Location location) {
        return queryFactory
                .select(Projections.fields(RecentReviewsDto.class,
                        review.content,
                        review.rating,
                        review.images))
                .from(review)
                .where(review.locationReview.id.eq(location.getId()))
                .orderBy(review.updatedAt.desc())
                .limit(3)
                .fetch();
    }
}
