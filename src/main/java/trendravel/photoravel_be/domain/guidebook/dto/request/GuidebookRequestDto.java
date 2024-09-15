package trendravel.photoravel_be.domain.guidebook.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.enums.Region;

@Schema(description = "가이드북 생성 조회 수정 요청 DTO", 
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class GuidebookRequestDto {
    
    @Schema(description = "가이드북 ID")
    private Long id;
    @Schema(description = "유저 아이디")
    private Long userId;
    @Schema(description = "제목")
    private String title;
    @Schema(description = "내용")
    private String content;
    @Schema(description = "지역")
    private Region region;
    
}
