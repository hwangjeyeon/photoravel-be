package trendravel.photoravel_be.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.db.respository.review.ReviewRepository;
import trendravel.photoravel_be.db.respository.spot.SpotRepository;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SpotRepositoryTest {

    Location location;
    Spot spot;
    Review spotReview;

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
        spot = Spot
                .builder()
                .description("미디어랩스관입니다")
                .longitude(35.24)
                .latitude(46.61)
                .title("미디어랩스건물 방문")
                .images(image)
                .views(0)
                .build();
        spotReview = Review
                .builder()
                .reviewType(ReviewTypes.SPOT)
                .images(image)
                .rating(3.0)
                .content("생각보다 별론데요?")
                .build();
    }

    @Test
    @DisplayName("spotRepository에 잘 저장되는지 테스트")
    void saveRepository(){
        spotRepository.save(spot);
        Spot findSpot = spotRepository.findById(spot.getId()).get();

        assertThat(findSpot.getId()).isEqualTo(spot.getId());
        assertThat(findSpot.getTitle()).isEqualTo(spot.getTitle());
        assertThat(findSpot.getLatitude()).isEqualTo(spot.getLatitude());
        assertThat(findSpot.getLongitude()).isEqualTo(spot.getLongitude());
        assertThat(findSpot.getDescription()).isEqualTo(spot.getDescription());
        assertThat(findSpot.getImages()).isEqualTo(spot.getImages());
        assertThat(findSpot.getViews()).isEqualTo(spot.getViews());
    }


    @Test
    @DisplayName("Spot-Review 연관관계 연결이 잘 되는지 확인")
    void correlatedReviewTest(){
        spotRepository.save(spot);
        spotReview.setSpotReview(spot);
        reviewRepository.save(spotReview);

        Spot findSpot = spotRepository.findById(spot.getId()).get();
        Review findReview = reviewRepository.findById(spotReview.getId()).get();

        assertThat(findSpot.getReviews().get(0).getRating()).isEqualTo(spotReview.getRating());
        assertThat(findSpot.getReviews().get(0).getContent()).isEqualTo(spotReview.getContent());
        assertThat(findSpot.getReviews().get(0).getReviewType()).isEqualTo(ReviewTypes.SPOT);
        assertThat(findSpot.getReviews().get(0).getImages()).isEqualTo(spotReview.getImages());
        assertThat(findSpot.getReviews().get(0).getId()).isEqualTo(spotReview.getId());


        assertThat(findReview.getSpotReview().getId()).isEqualTo(spot.getId());
    }


}
