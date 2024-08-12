package trendravel.photoravel_be.domain.spot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SpotResponseDto {

    @Schema(description = "스팟 ID")
    private Long spotId;
    @Schema(description = "스팟 제목")
    private String title;
    @Schema(description = "스팟 내용")
    private String description;
    @Schema(description = "스팟 위도")
    private double latitude;
    @Schema(description = "스팟 경도")
    private double longitude;
    @Schema(description = "스팟 이미지들")
    private List<String> images;

    // 유저 객체 전달 필요
    @Schema(description = "스팟 생성일")
    private LocalDateTime createdAt;
    @Schema(description = "스팟 수정일")
    private LocalDateTime updatedAt;

}
