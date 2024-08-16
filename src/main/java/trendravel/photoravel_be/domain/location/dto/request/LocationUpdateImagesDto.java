package trendravel.photoravel_be.domain.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.MediaType;

import java.util.List;


@Schema(description = "장소 생성/수정 요청 DTO", contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class LocationUpdateImagesDto {

    @Schema(description = "장소ID")
    private Long locationId;
    @Schema(description = "위도")
    private double latitude;
    @Schema(description = "경도")
    private double longitude;
    @Schema(description = "주소")
    private String address;
    @Schema(description = "설명")
    private String description;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "유저명")
    private String userId;

    private List<String> deleteImages;

}
