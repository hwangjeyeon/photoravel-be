package trendravel.photoravel_be.db.review;


import jakarta.persistence.*;
import lombok.*;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.domain.review.dto.request.ReviewRequestDto;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "REVIEW")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReviewTypes reviewType;

    private String content;
    private double rating;

    @ElementCollection
    @CollectionTable(
            name = "review_images",
            joinColumns = @JoinColumn(name = "review_id")
    )
    private List<String> images;

    // 회원, 가이드 관련 연관관계 필드 추가 필요


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location locationReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spotReview;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id")
    private Photographer photographerReview;
    
    //연관관계 편의 메소드
    public void setSpotReview(Spot spot) {
        this.spotReview = spot;
        spot.getReviews().add(this);
    }

    public void setLocationReview(Location location) {
        this.locationReview = location;
        location.getReview().add(this);
    }

    public void updateReview(ReviewRequestDto review, List<String> images) {
        this.content = review.getContent();
        this.rating = review.getRating();
        this.images = images;
    }

    public void updateReview(ReviewRequestDto review) {
        this.content = review.getContent();
        this.rating = review.getRating();
    }

}
