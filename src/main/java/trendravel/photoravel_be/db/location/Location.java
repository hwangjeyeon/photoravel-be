package trendravel.photoravel_be.db.location;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.db.enums.Category;
import trendravel.photoravel_be.db.member.MemberEntity;
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

    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;
    @Column(length = 50, nullable = false)
    private String address;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(length = 50, nullable = false)
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
    @Column(nullable = false)
    @ColumnDefault("0")
    private int views;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;


    @OneToMany(mappedBy = "location", orphanRemoval = true)
    @Builder.Default
    private List<Spot> spot = new ArrayList<>();

    @OneToMany(mappedBy = "locationReview", orphanRemoval = true)
    @Builder.Default
    private List<Review> review = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberEntity_id")
    private MemberEntity member;


    public void setMemberLocation(MemberEntity member) {
        this.member = member;
        member.getLocationMember().add(this);
    }

    public void updateLocation(LocationUpdateImagesDto location, List<String> newImages){
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.address = location.getAddress();
        this.description = location.getDescription();
        this.name = location.getName();
        this.point = new GeometryFactory()
                .createPoint(new Coordinate(longitude, latitude));
        this.point.setSRID(4326);
        if(location.getDeleteImages() != null){
            for (String deleteImage : location.getDeleteImages()) {
                this.images.remove(deleteImage);
            }
        }
        if(!newImages.isEmpty()){
            this.images.addAll(newImages);
        }
        this.category = location.getCategory();
    }

    public void updateLocation(LocationUpdateImagesDto location){
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.address = location.getAddress();
        this.description = location.getDescription();
        this.name = location.getName();
        this.point = new GeometryFactory()
                .createPoint(new Coordinate(longitude, latitude));
        this.point.setSRID(4326);
        if(location.getDeleteImages() != null){
            for (String deleteImage : location.getDeleteImages()) {
                this.images.remove(deleteImage);
            }
        }
        this.category = location.getCategory();
    }

}
