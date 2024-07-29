package trendravel.photoravel_be.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "SPOT")
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id")
    private Long id;


    private String title;
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

    @OneToMany(mappedBy = "spotReview")
    private List<Review> reviews = new ArrayList<>();

    //연관관계 편의 메소드
    public void setLocation(Location location){
        this.location = location;
        location.getSpot().add(this);
    }


}
