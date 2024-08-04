package trendravel.photoravel_be.domain.review.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReviewResponseDto {

    private Long ReviewId;
    private String reviewType;
    private String content;
    private double rating;
    private List<String> images;

    // 유저 객체 추가 필요

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

}
