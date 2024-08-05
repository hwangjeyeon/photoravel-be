package trendravel.photoravel_be.repository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.db.respository.review.ReviewRepository;
import trendravel.photoravel_be.db.respository.spot.SpotRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.db.spot.Spot;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class LocationRepositoryTest {

    Location location;
    Spot spot1;
    Review locationReview;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    SpotRepository spotRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @BeforeEach
    void before(){
        List<String> image = new ArrayList<>();
        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg");
        location = Location
                .builder()
                .name("순천향대학교")
                .latitude(35.24)
                .longitude(46.61)
                .address("아산시 신창면 순천향로46")
                .description("순천향대학교입니다.")
                .images(image)
                .views(0)
                .build();
        spot1 = Spot
                .builder()
                .description("미디어랩스관입니다")
                .longitude(35.24)
                .latitude(46.61)
                .title("미디어랩스건물 방문")
                .images(image)
                .views(0)
                .build();
        locationReview = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .images(image)
                .rating(4.0)
                .content("이 장소 좋네요")
                .build();
    }

    @Test
    @DisplayName("LocationRepository에 잘 저장되는지 테스트")
    void saveRepository(){
        locationRepository.save(location);
        Location findLocation = locationRepository.findById(location.getId()).get();

        assertThat(findLocation.getId()).isEqualTo(location.getId());
        assertThat(findLocation.getName()).isEqualTo(location.getName());
        assertThat(findLocation.getLatitude()).isEqualTo(location.getLatitude());
        assertThat(findLocation.getLongitude()).isEqualTo(location.getLongitude());
        assertThat(findLocation.getAddress()).isEqualTo(location.getAddress());
        assertThat(findLocation.getDescription()).isEqualTo(location.getDescription());
        assertThat(findLocation.getImages()).isEqualTo(location.getImages());
        assertThat(findLocation.getViews()).isEqualTo(location.getViews());
    }


    @Test
    @DisplayName("Location-Spot 연관관계 연결이 잘 되는지 확인")
    void correlatedSpotTest(){
        locationRepository.save(location);
        spot1.setLocation(location);
        spotRepository.save(spot1);

        Location findLocation = locationRepository.findById(location.getId()).get();
        Spot findSpot = spotRepository.findById(spot1.getId()).get();

        assertThat(findLocation.getSpot().get(0).getTitle()).isEqualTo(spot1.getTitle());
        assertThat(findLocation.getSpot().get(0).getDescription()).isEqualTo(spot1.getDescription());
        assertThat(findLocation.getSpot().get(0).getImages()).isEqualTo(spot1.getImages());
        assertThat(findLocation.getSpot().get(0).getViews()).isEqualTo(spot1.getViews());
        assertThat(findLocation.getSpot().get(0).getLatitude()).isEqualTo(spot1.getLatitude());
        assertThat(findLocation.getSpot().get(0).getLongitude()).isEqualTo(spot1.getLongitude());

        assertThat(findSpot.getLocation().getId()).isEqualTo(findLocation.getId());
    }

    @Test
    @DisplayName("Location-Review 연관관계 연결이 잘 되는지 확인")
    void correlatedReviewTest(){
        locationRepository.save(location);
        locationReview.setLocationReview(location);
        reviewRepository.save(locationReview);

        Location findLocation = locationRepository.findById(location.getId()).get();
        Review findLocationReview = reviewRepository.findById(locationReview.getId()).get();

        assertThat(findLocation.getReview().get(0).getId()).isEqualTo(locationReview.getId());
        assertThat(findLocation.getReview().get(0).getRating()).isEqualTo(locationReview.getRating());
        assertThat(findLocation.getReview().get(0).getContent()).isEqualTo(locationReview.getContent());
        assertThat(findLocation.getReview().get(0).getImages()).isEqualTo(locationReview.getImages());
        assertThat(findLocation.getReview().get(0).getReviewType()).isEqualTo(ReviewTypes.LOCATION);

        assertThat(findLocationReview.getLocationReview().getId()).isEqualTo(findLocation.getId());
    }




}