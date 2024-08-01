package trendravel.photoravel_be.dto.response.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class LocationResponseDto {

    private Long LocationId;
    private double latitude;
    private double longitude;
    private String address;
    private String description;
    private String name;
    private List<String> images;

    //유저 객체 추가 필요


    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

}
