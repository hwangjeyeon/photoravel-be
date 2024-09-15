package trendravel.photoravel_be.domain.photographer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.MediaType;

@Schema(description = "사진작가 로그인 요청 DTO", 
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class PhotographerLoginRequestDto {
    
    @Schema(description = "사진작가 ID")
    private String username;
    @Schema(description = "사진작가 비밀번호")
    private String password;
}
