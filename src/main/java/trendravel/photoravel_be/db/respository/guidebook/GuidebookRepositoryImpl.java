package trendravel.photoravel_be.db.respository.guidebook;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.guidebook.Guidebook;
import trendravel.photoravel_be.db.match.Matching;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.domain.guidebook.dto.response.GuidebookListResponseDto;
import trendravel.photoravel_be.domain.matching.dto.response.MatchingResponseDto;

import java.util.List;

import static trendravel.photoravel_be.db.guidebook.QGuidebook.guidebook;
import static trendravel.photoravel_be.db.photographer.QPhotographer.photographer;

@RequiredArgsConstructor
public class GuidebookRepositoryImpl implements GuidebookRepositoryCustom {
    
    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<Guidebook> getGuidebookByViews() {
        List<Guidebook> guidebookList = queryFactory
                .selectFrom(guidebook)
                .orderBy(guidebook.views.desc())  
                .limit(20)  
                .fetch();
        
        return guidebookList;
    }
    
    @Override
    public List<Guidebook> getGuidebookByNewest() {
        List<Guidebook> guidebookList = queryFactory
                .selectFrom(guidebook)
                .orderBy(guidebook.createdAt.desc())
                .limit(20) 
                .fetch();
        
        return guidebookList;
    }
    
    @Override
    public List<Guidebook> getGuidebookByRegion(Region region) {
        
        List<Guidebook> guidebookList = queryFactory
                .selectFrom(guidebook)
                .where(guidebook.region.eq(region))
                .orderBy(guidebook.views.desc())
                .limit(20)  
                .fetch();
        
        return guidebookList;
    }
    
    
}
    
    

