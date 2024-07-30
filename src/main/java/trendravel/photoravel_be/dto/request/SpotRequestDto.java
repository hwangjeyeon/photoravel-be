package trendravel.photoravel_be.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SpotRequestDto {

    private Long SpotId;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private List<String> images;
    private Long LocationId;
    private String userId;

}
