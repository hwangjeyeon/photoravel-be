package trendravel.photoravel_be.db.respository.photographer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.List;

import static trendravel.photoravel_be.db.photographer.QPhotographer.photographer;
import static trendravel.photoravel_be.db.review.QReview.review;

@RequiredArgsConstructor
public class PhotographerRepositoryImpl implements PhotographerRepositoryCustom {
    
    private final JPAQueryFactory queryFactory;
    
    public List<RecentReviewsDto> recentReviews(Long id) {
        List<Review> recentReviews = queryFactory
                .select(review)
                .from(review)
                .innerJoin(review.photographerReview, photographer)
                .where(photographer.id.eq(id))
                .orderBy(review.updatedAt.desc())
                .limit(3)
                .fetch();
        
        return recentReviews.stream()
                .map(p -> new RecentReviewsDto(p.getContent(),
                        p.getRating(), p.getImages(), p.getMember().getNickname()))
                .toList();
    }
    
    @Override
    public List<Photographer> getPhotographerByCareer() {
        
        List<Photographer> photographerList = queryFactory
                .selectFrom(photographer)
                .orderBy(photographer.careerYear.desc())
                .limit(20)
                .fetch();
        
        return photographerList;
    }
    
    @Override
    public List<Photographer> getPhotographerByMatchingCount() {
        
        List<Photographer> photographerList = queryFactory
                .selectFrom(photographer)
                .orderBy(photographer.matchingCount.desc())
                .limit(20)
                .fetch();
        
        return photographerList;
    }
    
    @Override
    public List<Photographer> getPhotographerByRegion(Region region) {
        
        List<Photographer> photographerList = queryFactory
                .selectFrom(photographer)
                .where(photographer.region.eq(region))  
                .orderBy(photographer.matchingCount.desc())  
                .limit(20)  // 상위 20개만 선택
                .fetch();
        
        return photographerList;
    }
    
    
}
