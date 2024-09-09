package trendravel.photoravel_be.domain.guidebook.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.enums.Region;

import java.util.List;

@Schema(description = "가이드북 UPDATE 요청 DTO (이미지 포함)", 
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class GuidebookUpdateImageDto {
    
    @Schema(description = "가이드북 ID")
    private Long id;
    @Schema(description = "제목")
    private String title;
    @Schema(description = "내용")
    private String content;
    @Schema(description = "지역")
    private Region region;
    @Schema(description = "삭제할 이미지 목록")
    private List<String> deleteImages;
    
}
