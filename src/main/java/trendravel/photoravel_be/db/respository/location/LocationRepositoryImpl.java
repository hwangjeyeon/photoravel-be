package trendravel.photoravel_be.db.respository.location;

import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.location.dto.request.LocationKeywordDto;
import trendravel.photoravel_be.domain.location.dto.request.LocationNowPositionDto;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;


import java.util.List;


import static java.util.Objects.isNull;
import static trendravel.photoravel_be.db.location.QLocation.location;
import static trendravel.photoravel_be.db.review.QReview.review;

@RequiredArgsConstructor
@Slf4j
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
                        p.getRating(), p.getImages()
                        , p.getMember().getNickname()))
                .toList();
    }

    @Override
    public List<Location> searchNowPosition(
            LocationNowPositionDto locationNowPositionDto) {
        return queryFactory
                .select(location)
                .from(location)
                .where(inRangeDistance(locationNowPositionDto.getLatitude(),
                        locationNowPositionDto.getLongitude(),
                        locationNowPositionDto.getRange()))
                .fetch();
    }

    @Override
    public List<Location> searchKeyword(LocationKeywordDto locationKeywordDto) {
        return queryFactory
                .select(location)
                .from(location)
                .where(location.name.contains(locationKeywordDto.getKeyword()),
                        inRangeDistance(locationKeywordDto.getLatitude(),
                                locationKeywordDto.getLongitude(),
                                locationKeywordDto.getRange()))
                .fetch();
    }



    private BooleanTemplate inRangeDistance(
            double latitude, double longitude, double range) {
        Point points = new GeometryFactory()
                .createPoint(new Coordinate(longitude
                        , latitude));

        if(isNull(points)){
            return null;
        }
        points.setSRID(4326);

        return Expressions.booleanTemplate("ST_Contains(ST_BUFFER({0}, {1}), {2})",
                points, range, location.point);
    }

    @Override
    public void increaseViews(Long id) {
        queryFactory.update(location)
                .set(location.views, location.views.add(1))
                .where(location.id.eq(id))
                .execute();
    }
}
