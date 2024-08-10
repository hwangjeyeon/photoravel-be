package trendravel.photoravel_be.db.location;


import jakarta.persistence.*;
import lombok.*;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.domain.location.dto.request.LocationRequestDto;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.spot.Spot;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "LOCATION")
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;


    private double latitude;
    private double longitude;
    private String address;
    private String description;
    private String name;



    @ElementCollection
    @CollectionTable(
            name = "location_images",
            joinColumns = @JoinColumn(name = "location_id")
    )
    private List<String> images = new ArrayList<>();
    private int views;

    //유저 엔티티 생성 후, 연관관계 필드 추가 필요


    @OneToMany(mappedBy = "location")
    @Builder.Default
    private List<Spot> spot = new ArrayList<>();

    @OneToMany(mappedBy = "locationReview")
    @Builder.Default
    private List<Review> review = new ArrayList<>();

    public void updateLocation(LocationRequestDto location, List<String> images){
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.address = location.getAddress();
        this.description = location.getDescription();
        this.name = location.getName();
        this.images = images;
    }

    public void updateLocation(LocationRequestDto location){
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.address = location.getAddress();
        this.description = location.getDescription();
        this.name = location.getName();
    }

    public void increaseViews(){
        this.views++;
    }

}
