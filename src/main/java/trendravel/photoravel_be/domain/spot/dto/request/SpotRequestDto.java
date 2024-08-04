package trendravel.photoravel_be.domain.spot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.MediaType;


@Schema(description = "스팟 생성/수정 요청 DTO", contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class SpotRequestDto {

    @Schema(description = "스팟 ID")
    private Long spotId;
    @Schema(description = "스팟 제목")
    private String title;
    @Schema(description = "스팟 내용")
    private String description;
    @Schema(description = "리뷰 위도")
    private double latitude;
    @Schema(description = "리뷰 경도")
    private double longitude;
    @Schema(description = "장소 ID")
    private Long locationId;
    @Schema(description = "유저 아이디")
    private String userId;

}
