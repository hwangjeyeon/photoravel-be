package trendravel.photoravel_be.domain.location.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationNowPositionDto {

    private double latitude;
    private double longitude;
    private double range;

}
