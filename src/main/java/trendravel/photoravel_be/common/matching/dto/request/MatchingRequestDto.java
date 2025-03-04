package trendravel.photoravel_be.common.matching.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.enums.Region;

@Schema(description = "매칭 요청 DTO", 
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class MatchingRequestDto {
    
    @Schema(description = "유저 계정 ID")
    private String memberId;
    @Schema(description = "작가 계정 ID")
    private String photographerId;
    
}
