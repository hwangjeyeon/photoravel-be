package trendravel.photoravel_be.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import trendravel.photoravel_be.entity.Location;
import trendravel.photoravel_be.entity.Review;
import trendravel.photoravel_be.entity.Spot;
import trendravel.photoravel_be.entity.enums.ReviewTypes;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    Location location;
    Spot spot;
    Review spotReview;
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
                .spotReview(spot)
                .build();

        locationReview = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .locationReview(location)
                .rating(1.5)
                .content("진짜 최악 ㅡㅡ")
                .images(image)
                .build();
    }

    @Test
    @DisplayName("reviewRepository에 잘 저장되는지 테스트")
    void saveRepository(){
        Review findSpotReview = reviewRepository.save(spotReview);
        Review findLocationReview = reviewRepository.save(locationReview);


        assertThat(findSpotReview.getId()).isEqualTo(spotReview.getId());
        assertThat(findSpotReview.getContent()).isEqualTo(spotReview.getContent());
        assertThat(findSpotReview.getRating()).isEqualTo(spotReview.getRating());
        assertThat(findSpotReview.getReviewType()).isEqualTo(spotReview.getReviewType());
        assertThat(findSpotReview.getImages()).isEqualTo(spotReview.getImages());
        assertThat(findSpotReview.getSpotReview()).isEqualTo(spotReview.getSpotReview());

        assertThat(findLocationReview.getId()).isEqualTo(locationReview.getId());
        assertThat(findLocationReview.getContent()).isEqualTo(locationReview.getContent());
        assertThat(findLocationReview.getRating()).isEqualTo(locationReview.getRating());
        assertThat(findLocationReview.getReviewType()).isEqualTo(locationReview.getReviewType());
        assertThat(findLocationReview.getImages()).isEqualTo(locationReview.getImages());
        assertThat(findLocationReview.getLocationReview()).isEqualTo(locationReview.getLocationReview());
    }



}