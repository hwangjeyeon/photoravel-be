package trendravel.photoravel_be.dto.request;

import lombok.*;

import java.util.List;

@Data
public class LocationRequestDto {

    private Long locationId;
    private double latitude;
    private double longitude;
    private String address;
    private String description;
    private String name;
    private List<String> images;
    private String userId;

}
