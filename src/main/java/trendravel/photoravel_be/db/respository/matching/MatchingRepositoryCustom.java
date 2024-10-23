package trendravel.photoravel_be.db.respository.matching;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trendravel.photoravel_be.db.match.Matching;
import trendravel.photoravel_be.db.match.enums.MatchingStatus;
import trendravel.photoravel_be.domain.matching.dto.response.MatchingResponseDto;

import java.util.List;
import java.util.Optional;

public interface MatchingRepositoryCustom {
    
    List<MatchingResponseDto> getMatchingListByMemberId(String memberId);
    
    List<MatchingResponseDto> getMatchingListByPhotographerId(String photographerId);
    
}
