package trendravel.photoravel_be.domain.review.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;

import java.util.List;


@Schema(description = "리뷰 CREATE/UPDATE(이미지 포함) 요청 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class ReviewUpdateImagesDto {

    @Schema(description = "리뷰ID")
    private Long reviewId;
    @Schema(description = "리뷰타입")
    private ReviewTypes reviewType;
    @Schema(description = "리뷰내용")
    @NotBlank(message = "공백/null 입력은 미허용됩니다.")
    @Length(max = 500, message = "최대 길이는 500자 입니다.")
    private String content;
    @Schema(description = "리뷰평점")
    @DecimalMax(value = "5.0", message = "최대 허용 별점은 5.0입니다.")
    @DecimalMin(value = "1.0", message = "최소 허용 별점은 1.0입니다.")
    private double rating;
    @Schema(description = "유저아이디")
    private String userId;
    @Schema(description = "리뷰타입(장소/스팟)ID")
    private Long typeId;
    @Schema(description = "삭제할 이미지 목록")
    private List<String> deleteImages;

}
