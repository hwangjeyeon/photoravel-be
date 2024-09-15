package trendravel.photoravel_be.db.respository.matching;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import trendravel.photoravel_be.db.match.Matching;
import trendravel.photoravel_be.domain.matching.dto.response.MatchingResponseDto;

import java.util.List;

import static trendravel.photoravel_be.db.match.QMatching.matching;

@RequiredArgsConstructor
public class MatchingRepositoryImpl implements MatchingRepositoryCustom {
    
    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<MatchingResponseDto> getMatchingListByMemberId(String memberId) {
        
        List<Matching> matchingList = queryFactory
                .selectFrom(matching)
                .where(matching.memberId.eq(memberId))
                .orderBy(matching.id.desc())
                .fetch();
        
        return matchingList.stream()
                .map(m -> MatchingResponseDto.builder()
                        .memberId(m.getMemberId())
                        .photographerId(m.getPhotographerId())
                        .matchingStatus(m.getStatus())
                        .build()
                ).toList();
    }
    
    @Override
    public List<MatchingResponseDto> getMatchingListByPhotographerId(String photographerId) {
        
        List<Matching> matchingList = queryFactory
                .selectFrom(matching)
                .where(matching.photographerId.eq(photographerId))
                .orderBy(matching.id.desc())
                .fetch();
        
        return matchingList.stream()
                .map(m -> MatchingResponseDto.builder()
                        .memberId(m.getMemberId())
                        .photographerId(m.getPhotographerId())
                        .matchingStatus(m.getStatus())
                        .build()
                ).toList();
    }
    
}
