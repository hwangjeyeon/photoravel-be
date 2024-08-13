package trendravel.photoravel_be.domain.guide.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;

@Data
@Builder
@Schema(description = "장소 로그인 요청 DTO", 
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class GuideLoginResponseDto {
    
    @Schema(description = "가이드 계정 id")
    private String username;
    @Schema(description = "비밀번호")
    private String password;
}
