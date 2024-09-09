package trendravel.photoravel_be.domain.guidebook.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.enums.Region;

@Schema(description = "가이드북 UPDATE 요청 DTO (이미지 미포함)", 
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class GuidebookUpdateDto {
    
    @Schema(description = "가이드북 ID")
    private Long id;
    @Schema(description = "제목")
    private String title;
    @Schema(description = "내용")
    private String content;
    @Schema(description = "지역")
    private Region region;
    
}
