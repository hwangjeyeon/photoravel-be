package trendravel.photoravel_be.domain.spot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "스팟 CREATE/UPDATE 응답 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class SpotResponseDto {

    @Schema(description = "스팟 ID")
    private Long spotId;
    @Schema(description = "스팟 제목")
    private String title;
    @Schema(description = "스팟 내용")
    private String description;
    @Schema(description = "스팟 위도")
    private Double latitude;
    @Schema(description = "스팟 경도")
    private Double longitude;
    @Schema(description = "스팟 이미지들")
    private List<String> images;

    @Schema(description = "스팟 조회수")
    private Integer views;

    @Schema(description = "스팟 작성 유저")
    private String userName;
    @Schema(description = "스팟 생성일")
    private LocalDateTime createdAt;
    @Schema(description = "스팟 수정일")
    private LocalDateTime updatedAt;

}
