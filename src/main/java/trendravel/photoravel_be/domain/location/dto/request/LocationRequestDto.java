package trendravel.photoravel_be.domain.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.MediaType;


@Data
@Schema(description = "장소 생성/수정 요청 DTO", contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class LocationRequestDto {

    @Schema(description = "장소ID")
    private Long locationId;
    private double latitude;
    private double longitude;
    private String address;
    private String description;
    private String name;
    private String userId;

}
