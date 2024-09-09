package trendravel.photoravel_be.db.location;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.domain.location.dto.request.LocationRequestDto;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.spot.Spot;
import org.locationtech.jts.geom.Point;
import trendravel.photoravel_be.domain.location.dto.request.LocationUpdateImagesDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "LOCATION", indexes = {
        @Index(name = "idx__point", columnList = "point"),
        @Index(name = "idx__point__name", columnList = "name")
})
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    private Double latitude;
    private Double longitude;
    @Column(length = 50)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String name;

    @Column(columnDefinition = "POINT SRID 4326", nullable = false)
    private Point point;

    @ElementCollection
    @CollectionTable(
            name = "location_images",
            joinColumns = @JoinColumn(name = "location_id")
    )
    @Builder.Default
    private List<String> images = new ArrayList<>();
    private int views;

    //유저 엔티티 생성 후, 연관관계 필드 추가 필요


    @OneToMany(mappedBy = "location", orphanRemoval = true)
    @Builder.Default
    private List<Spot> spot = new ArrayList<>();

    @OneToMany(mappedBy = "locationReview", orphanRemoval = true)
    @Builder.Default
    private List<Review> review = new ArrayList<>();


    public void updateLocation(LocationUpdateImagesDto location, List<String> newImages){
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.address = location.getAddress();
        this.description = location.getDescription();
        this.name = location.getName();
        this.point = new GeometryFactory()
                .createPoint(new Coordinate(latitude, longitude));
        this.point.setSRID(4326);
        this.images.removeAll(location.getDeleteImages());
        this.images.addAll(newImages);
    }

    public void updateLocation(LocationRequestDto location){
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.address = location.getAddress();
        this.description = location.getDescription();
        this.name = location.getName();
        this.point = new GeometryFactory()
                .createPoint(new Coordinate(latitude, longitude));
        this.point.setSRID(4326);
    }

    public void increaseViews(){
        this.views++;
    }

}
