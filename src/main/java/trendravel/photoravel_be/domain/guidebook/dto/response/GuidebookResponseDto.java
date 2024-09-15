package trendravel.photoravel_be.domain.guidebook.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.enums.Region;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "가이드북 생성 조회 수정 응답 DTO", 
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class GuidebookResponseDto {
    
    @Schema(description = "가이드북 ID")
    private Long id;
    @Schema(description = "유저 ID")
    private String userId;
    @Schema(description = "제목")
    private String title;
    @Schema(description = "내용")
    private String content;
    @Schema(description = "지역")
    private Region region;
    
    @Schema(description = "조회수")
    private int views;
    
    @Schema(description = "이미지")
    private List<String> images;
    
    @Schema(description = "가이드북 생성일")
    private LocalDateTime createdAt;
    @Schema(description = "가이드북 수정일")
    private LocalDateTime updatedAt;
    
}
