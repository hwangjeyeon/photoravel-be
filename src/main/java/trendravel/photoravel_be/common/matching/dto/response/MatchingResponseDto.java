package trendravel.photoravel_be.common.matching.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.match.enums.MatchingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "매칭 응답 DTO", 
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class MatchingResponseDto {
    
    @Schema(description = "유저 계정 ID")
    private String memberId;
    @Schema(description = "작가 계정 ID")
    private String photographerId;
    @Schema(description = "매칭 상태")
    private MatchingStatus matchingStatus;
    
}
