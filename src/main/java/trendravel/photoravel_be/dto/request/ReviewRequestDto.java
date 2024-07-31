package trendravel.photoravel_be.dto.request;


import lombok.Data;
import trendravel.photoravel_be.entity.enums.ReviewTypes;



@Data
public class ReviewRequestDto {

    private Long reviewId;
    private ReviewTypes reviewType;
    private String content;
    private double rating;
    private String userId;
    private Long typeId;

}
