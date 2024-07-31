package trendravel.photoravel_be.dto.request;

import lombok.Data;



@Data
public class SpotRequestDto {

    private Long SpotId;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private Long LocationId;
    private String userId;

}
