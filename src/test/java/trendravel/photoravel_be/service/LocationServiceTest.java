package trendravel.photoravel_be.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import trendravel.photoravel_be.commom.image.service.ImageService;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.respository.review.ReviewRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.domain.location.dto.response.LocationSingleReadResponseDto;
import trendravel.photoravel_be.domain.location.service.LocationService;
import trendravel.photoravel_be.domain.location.dto.request.LocationRequestDto;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LocationServiceTest {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    EntityManager em;

    @MockBean
    LocationService locationService;
    @MockBean
    ImageService imageService;

    LocationRequestDto locationRequestDto;
    Location location;
    Review review1;
    Review review2;
    Review review3;
    Review review4;

    @BeforeEach
    void before(){
        locationService = new LocationService(locationRepository, imageService);
        location = Location
                .builder()
                .name("순천향대학교")
                .latitude(35.24)
                .longitude(46.61)
                .address("아산시 신창면 순천향로46")
                .description("순천향대학교입니다.")
                .views(0)
                .build();
        locationRepository.save(location);
        review1 = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .content("멋지네")
                .rating(1.5)
                .locationReview(location)
                .build();
        review2 = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .content("키야")
                .rating(2.4)
                .locationReview(location)
                .build();
        review3 = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .content("이야")
                .rating(3.42)
                .locationReview(location)
                .build();
        review4 = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .content("그저 굿")
                .rating(4.5)
                .locationReview(location)
                .build();
        review1.setLocationReview(location);
        review2.setLocationReview(location);
        review3.setLocationReview(location);
        review4.setLocationReview(location);

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);


        locationRequestDto = new LocationRequestDto();
        locationRequestDto.setLocationId(1L);
        locationRequestDto.setName("순천향대학교");
        locationRequestDto.setAddress("아산시 신창면 순천향로46");
        locationRequestDto.setLatitude(35.24);
        locationRequestDto.setLongitude(46.61);
        locationRequestDto.setDescription("순천향대학교입니다.");

    }

    @Test
    @DisplayName("Location CREATE (이미지 미포함) 서비스가 잘 작동하는지 테스트")
    void createLocationServiceTest(){
        locationService.createLocation(locationRequestDto);

        assertThat(locationRepository.findById(
                locationRequestDto.getLocationId())
                .get().getId()).isEqualTo(1L);
        assertThat(locationRepository.findById(
                locationRequestDto.getLocationId())
                .get().getName()).isEqualTo("순천향대학교");
        assertThat(locationRepository.findById(
                locationRequestDto.getLocationId())
                .get().getAddress()).isEqualTo("아산시 신창면 순천향로46");
        assertThat(locationRepository.findById(
                locationRequestDto.getLocationId())
                .get().getLatitude()).isEqualTo(35.24);
        assertThat(locationRepository.findById(
                locationRequestDto.getLocationId())
                .get().getLongitude()).isEqualTo(46.61);
        assertThat(locationRepository.findById(
                locationRequestDto.getLocationId())
                .get().getDescription()).isEqualTo("순천향대학교입니다.");
    }


    @Test
    @DisplayName("Location UPDATE (이미지 미포함) 서비스가 잘 작동하는지 테스트")
    void updateLocationServiceTest(){
        locationService.createLocation(locationRequestDto);
        locationRequestDto.setName("미디어랩스");
        locationService.updateLocation(locationRequestDto);

        assertThat(locationRepository.findById(
                        locationRequestDto.getLocationId())
                .get().getId()).isEqualTo(1L);
        assertThat(locationRepository.findById(
                        locationRequestDto.getLocationId())
                .get().getName()).isEqualTo("미디어랩스");
        assertThat(locationRepository.findById(
                        locationRequestDto.getLocationId())
                .get().getAddress()).isEqualTo("아산시 신창면 순천향로46");
        assertThat(locationRepository.findById(
                        locationRequestDto.getLocationId())
                .get().getLatitude()).isEqualTo(35.24);
        assertThat(locationRepository.findById(
                        locationRequestDto.getLocationId())
                .get().getLongitude()).isEqualTo(46.61);
        assertThat(locationRepository.findById(
                        locationRequestDto.getLocationId())
                .get().getDescription()).isEqualTo("순천향대학교입니다.");
    }

    @Test
    @DisplayName("Location DELETE 서비스가 잘 작동하는지 테스트")
    void deleteLocationServiceTest(){
        locationService.createLocation(locationRequestDto);
        locationService.deleteLocation(locationRequestDto.getLocationId());

        assertThat(locationRepository.findById(locationRequestDto.getLocationId())).isEmpty();
    }

    @Test
    @DisplayName("Location SINGLE READ 서비스가 잘 동작하는지 테스트")
    void readSingleLocationServiceTest(){
        //given
        locationService.createLocation(locationRequestDto);
        Location findLocation = locationRepository.findById(1L).orElse(null);

        //when
        LocationSingleReadResponseDto locationSingleReadResponseDto
                = locationService.readSingleLocation(1L);
        List<RecentReviewsDto> findRecentReviews =
                locationSingleReadResponseDto.getRecentReviewDtos();

        //then
        assertThat(locationSingleReadResponseDto.getLocationId())
                .isEqualTo(findLocation.getId());
        assertThat(locationSingleReadResponseDto.getLatitude())
                .isEqualTo(findLocation.getLatitude());
        assertThat(locationSingleReadResponseDto.getLongitude())
                .isEqualTo(findLocation.getLongitude());
        assertThat(locationSingleReadResponseDto.getDescription())
                .isEqualTo(findLocation.getDescription());
        assertThat(locationSingleReadResponseDto.getAddress())
                .isEqualTo(findLocation.getAddress());
        assertThat(locationSingleReadResponseDto.getRatingAvg())
                .isEqualTo(String.format("%.2f",
                        (review4.getRating() + review2.getRating()
                                + review3.getRating() + review1.getRating()) / 4));
        assertThat(locationSingleReadResponseDto.getViews()).isEqualTo(1);
        assertThat(findRecentReviews).extracting("rating")
                .containsExactlyInAnyOrder(review4.getRating(),
                        review2.getRating(), review3.getRating());
    }


}