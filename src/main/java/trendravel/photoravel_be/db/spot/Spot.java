package trendravel.photoravel_be.db.spot;


import jakarta.persistence.*;
import lombok.*;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.domain.spot.dto.request.SpotRequestDto;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.spot.dto.request.SpotUpdatedImagesDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "SPOT")
public class Spot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id")
    private Long id;


    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    private double latitude;
    private double longitude;
    private int views;

    @ElementCollection
    @CollectionTable(
            name = "spot_image",
            joinColumns = @JoinColumn(name = "spot_id")
    )
    private List<String> images;



    //유저 엔티티 생성 후, 연관관계 필드 추가 필요


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "spotReview", orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    //연관관계 편의 메소드
    public void setLocation(Location location){
        this.location = location;
        location.getSpot().add(this);
    }

    public void updateSpot(SpotUpdatedImagesDto spot, List<String> images){
        this.title = spot.getTitle();
        this.description = spot.getDescription();
        this.latitude = spot.getLatitude();
        this.longitude = spot.getLongitude();
        for (String deleteImage : spot.getDeleteImages()) {
            this.images.remove(deleteImage);
        }
        this.images.addAll(images);
    }

    public void updateSpot(SpotRequestDto spot){
        this.title = spot.getTitle();
        this.description = spot.getDescription();
        this.latitude = spot.getLatitude();
        this.longitude = spot.getLongitude();
    }

    public void increaseViews(){
        this.views++;
    }



}
