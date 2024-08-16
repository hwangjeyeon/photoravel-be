package trendravel.photoravel_be.domain.guide.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.enums.Region;

@Schema(description = "가이드 생성 조회 수정 요청 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class GuideRequestDto {
    
    @Schema(description = "가이드 ID")
    private Long id;
    @Schema(description = "가이드 계정 ID")
    private String accountId;
    @Schema(description = "비밀번호")
    private String password;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "지역")
    private Region region;
    @Schema(description = "설명")
    private String description;
    @Schema(description = "프로필 이미지")
    private String profileImg;
    
}
