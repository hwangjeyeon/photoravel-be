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
@Table(name = "LOCATION")
public class Location {

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
    private double distance;
    private int views;

    //유저 엔티티 생성 후, 연관관계 필드 추가 필요


    @OneToMany(mappedBy = "location")
    private List<Spot> spot = new ArrayList<>();

    @OneToMany(mappedBy = "locationReview")
    private List<Review> review = new ArrayList<>();



}
