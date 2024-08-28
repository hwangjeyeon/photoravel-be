package trendravel.photoravel_be.db.review;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.domain.review.dto.request.ReviewRequestDto;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.domain.review.dto.request.ReviewUpdateImagesDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "REVIEW")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReviewTypes reviewType;

    @Column(length = 500)
    @NotBlank(message = "공백/null 입력은 미허용됩니다.")
    @Length(max = 500, message = "최대 길이는 500자 입니다.")
    private String content;

    @DecimalMax(value = "5.0", message = "최대 허용 별점은 5.0입니다.")
    @DecimalMin(value = "1.0", message = "최소 허용 별점은 1.0입니다.")
    @NotNull(message = "필수 입력사항입니다.")
    private Double rating;

    @ElementCollection
    @CollectionTable(
            name = "review_images",
            joinColumns = @JoinColumn(name = "review_id")
    )
    @Size(max = 10, message = "한번에 들어올 수 있는 이미지는 10개입니다")
    private List<String> images = new ArrayList<>();

    // 회원, 가이드 관련 연관관계 필드 추가 필요


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location locationReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spotReview;

    //연관관계 편의 메소드
    public void setSpotReview(Spot spot) {
        this.spotReview = spot;
        spot.getReviews().add(this);
    }

    public void setLocationReview(Location location) {
        this.locationReview = location;
        location.getReview().add(this);
    }

    public void updateReview(ReviewUpdateImagesDto review, List<String> newImages) {
        this.content = review.getContent();
        this.rating = review.getRating();
        for (String deleteImage : review.getDeleteImages()) {
            this.images.remove(deleteImage);
        }
        this.images.addAll(newImages);
    }

    public void updateReview(ReviewRequestDto review) {
        this.content = review.getContent();
        this.rating = review.getRating();
    }

}
