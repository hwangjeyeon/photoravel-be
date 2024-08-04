package trendravel.photoravel_be.domain.spot.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SpotResponseDto {

    private Long spotId;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private List<String> images;

    // 유저 객체 전달 필요

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

}
