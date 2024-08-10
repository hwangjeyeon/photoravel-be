package trendravel.photoravel_be.domain.review.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentReviewsDto {

    private String content;
    private double rating;
    private List<String> images;

    // 유저 객체 필요

}
