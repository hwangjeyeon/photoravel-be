package trendravel.photoravel_be.db.respository.location;

import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.location.dto.request.LocationNowPositionDto;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;


import java.util.List;


import static java.util.Objects.isNull;
import static trendravel.photoravel_be.db.location.QLocation.location;
import static trendravel.photoravel_be.db.review.QReview.review;

@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    /**
     * 성능 최적화를 위한 fetch join 리팩토링 고민할 것
     */
    @Override
    public List<RecentReviewsDto> recentReviews(Long id) {
        List<Review> recentReviews = queryFactory
                .select(review)
                .from(review)
                .innerJoin(review.locationReview, location)
                .where(location.id.eq(id))
                .orderBy(review.updatedAt.desc())
                .limit(3)
                .fetch();

        return recentReviews.stream()
                .map(p -> new RecentReviewsDto(p.getContent(),
                        p.getRating(), p.getImages()))
                .toList();
    }

    @Override
    public List<Location> searchNowPosition(
            LocationNowPositionDto locationNowPositionDto) {
        return queryFactory
                .select(location)
                .from(location)
                .where(inRangeDistance(locationNowPositionDto))
                .fetch();

    }

    private BooleanTemplate inRangeDistance(
            LocationNowPositionDto locationNowPositionDto) {
        Point points = new GeometryFactory()
                .createPoint(new Coordinate(locationNowPositionDto.getLatitude()
                        , locationNowPositionDto.getLongitude()));

        if(isNull(points)){
            return null;
        }
        points.setSRID(4326);

        return Expressions.booleanTemplate("ST_Contains(ST_BUFFER({0}, {1}), {2})",
                points, locationNowPositionDto.getRange(), location.point);
    }

}
