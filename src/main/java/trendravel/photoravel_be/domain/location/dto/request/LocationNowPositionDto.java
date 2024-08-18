package trendravel.photoravel_be.domain.location.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "(현재 위치 기반) 주변 장소 검색 요청 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class LocationNowPositionDto {

    @Schema(description = "경도")
    private double latitude;
    @Schema(description = "위도")
    private double longitude;
    @Schema(description = "범위")
    private double range;

}