package trendravel.photoravel_be.domain.review.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "최근 업데이트된 리뷰 목록 (최대 3개)",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class RecentReviewsDto {

    @Schema(description = "리뷰 내용")
    private String content;
    @Schema(description = "리뷰 평점")
    private double rating;
    @Schema(description = "리뷰 이미지들")
    private List<String> images;

    @Schema(description = "리뷰 작성 유저")
    private String userName;

}
