package trendravel.photoravel_be.dto.request;

import lombok.Data;



@Data
public class SpotRequestDto {

    private Long spotId;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private Long locationId;
    private String userId;

}
