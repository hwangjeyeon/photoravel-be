package trendravel.photoravel_be.db.respository.guidebook;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.guidebook.Guidebook;
import trendravel.photoravel_be.db.guidebook.QGuidebook;

import java.util.List;

import static trendravel.photoravel_be.db.guidebook.QGuidebook.guidebook;

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
                .orderBy(guidebook.updatedAt.desc())
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
    
    @Override
    public void increaseViewCount(Long guidebookId) {
        QGuidebook guidebook = QGuidebook.guidebook;
        
        queryFactory.update(guidebook)
                .set(guidebook.views, guidebook.views.add(1))
                .where(guidebook.id.eq(guidebookId))
                .execute();
    }
    
    
}
    
    

