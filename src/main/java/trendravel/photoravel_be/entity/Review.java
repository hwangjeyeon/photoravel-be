package trendravel.photoravel_be.entity;


import jakarta.persistence.*;
import lombok.*;
import trendravel.photoravel_be.entity.enums.ReviewTypes;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Enum<ReviewTypes> reviewType;

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

    //연관관계 편의 메소드
    public void setSpotReview(Spot spot){
        this.spotReview = spot;
        spot.getReviews().add(this);
    }

    public void setLocationReview(Location location){
        this.locationReview = location;
        location.getReview().add(this);
    }

}
