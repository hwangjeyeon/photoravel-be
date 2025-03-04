package trendravel.photoravel_be.db.respository.matching;

import trendravel.photoravel_be.common.matching.dto.response.MatchingResponseDto;

import java.util.List;

public interface MatchingRepositoryCustom {
    
    List<MatchingResponseDto> getMatchingListByMemberId(String memberId);
    
    List<MatchingResponseDto> getMatchingListByPhotographerId(String photographerId);
    
}
