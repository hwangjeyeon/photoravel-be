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
@Schema(description = "가이드북 목록 READ시 응답 DTO", 
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class GuidebookListResponseDto {
    
    @Schema(description = "가이드북 ID")
    private Long id;
    @Schema(description = "유저 ID")
    private Long userId;
    @Schema(description = "제목")
    private String title;
    @Schema(description = "지역")
    private Region region;
    
    @Schema(description = "조회수")
    private int views;
    

    @Schema(description = "썸네일 이미지 URL (배열의 첫번째 이미지 즉, 업로드 할 때 첫번째 등록한 이미지를 반환)")
    private String image;

    
    @Schema(description = "가이드북 생성일")
    private LocalDateTime createdAt;
    @Schema(description = "가이드북 수정일")
    private LocalDateTime updatedAt;
    
}
