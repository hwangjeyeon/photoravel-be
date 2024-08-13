package trendravel.photoravel_be.domain.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.MediaType;


@Schema(description = "장소 CREATE/UPDATE(이미지 미포함) 요청 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class LocationRequestDto {

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

}
