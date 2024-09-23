package trendravel.photoravel_be.db.spot;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.domain.spot.dto.request.SpotRequestDto;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.spot.dto.request.SpotUpdatedImagesDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "SPOT")
public class Spot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id")
    private Long id;


    @Column(length = 50, nullable = false)
    @Length(max = 50, message="최대 길이는 50자 입니다.")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer views;

    @ElementCollection
    @CollectionTable(
            name = "spot_image",
            joinColumns = @JoinColumn(name = "spot_id")
    )
    @Builder.Default
    private List<String> images = new ArrayList<>();



    //유저 엔티티 생성 후, 연관관계 필드 추가 필요


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "spotReview", orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberEntity_id")
    private MemberEntity member;


    //연관관계 편의 메소드
    public void setLocation(Location location){
        this.location = location;
        location.getSpot().add(this);
    }

    public void setMemberSpot(MemberEntity member){
        this.member = member;
        member.getSpotMember().add(this);
    }

    public void updateSpot(SpotUpdatedImagesDto spot, List<String> images){
        this.title = spot.getTitle();
        this.description = spot.getDescription();
        this.latitude = spot.getLatitude();
        this.longitude = spot.getLongitude();
        this.images.removeAll(images);
        if(spot.getDeleteImages() != null){
            for (String deleteImage : spot.getDeleteImages()) {
                this.images.remove(deleteImage);
            }
        }
        if(!images.isEmpty()){
            this.images.addAll(images);
        }
    }

    public void updateSpot(SpotUpdatedImagesDto spot){
        this.title = spot.getTitle();
        this.description = spot.getDescription();
        this.latitude = spot.getLatitude();
        this.longitude = spot.getLongitude();
        if(spot.getDeleteImages() != null){
            for (String deleteImage : spot.getDeleteImages()) {
                this.images.remove(deleteImage);
            }
        }
    }

    public void increaseViews(){
        this.views++;
    }



}
