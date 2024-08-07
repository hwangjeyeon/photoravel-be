package trendravel.photoravel_be.domain.location.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationNowPositionDto {

    private double latitude;
    private double longitude;
    private double range;

}
