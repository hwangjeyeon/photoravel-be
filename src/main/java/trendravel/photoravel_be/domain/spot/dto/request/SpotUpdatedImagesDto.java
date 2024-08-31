package trendravel.photoravel_be.domain.spot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.MediaType;

import java.util.List;


@Schema(description = "스팟 UPDATE(이미지 포함) 요청 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class SpotUpdatedImagesDto {

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
    @Schema(description = "삭제할 이미지 목록")
    private List<String> deleteImages;

}
