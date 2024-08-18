package trendravel.photoravel_be.domain.review.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;



@Schema(description = "리뷰 CREATE/UPDATE(이미지 미포함) 요청 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class ReviewRequestDto {

    @Schema(description = "리뷰ID")
    private Long reviewId;
    @Schema(description = "리뷰타입")
    private ReviewTypes reviewType;
    @Schema(description = "리뷰내용")
    private String content;
    @Schema(description = "리뷰평점")
    private double rating;
    @Schema(description = "유저아이디")
    private String userId;
    @Schema(description = "리뷰타입(장소/스팟)ID")
    private Long typeId;

}
