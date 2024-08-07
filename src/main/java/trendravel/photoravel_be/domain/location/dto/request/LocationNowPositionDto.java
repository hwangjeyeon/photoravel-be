package trendravel.photoravel_be.domain.location.dto.request;


import lombok.Data;

@Data
public class LocationNowPositionDto {

    private double latitude;
    private double longitude;
    private double range;

}
