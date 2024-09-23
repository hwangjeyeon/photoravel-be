package trendravel.photoravel_be.service;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import trendravel.photoravel_be.commom.error.LocationErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.commom.image.service.ImageServiceFacade;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.db.respository.review.ReviewRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.domain.location.dto.request.LocationKeywordDto;
import trendravel.photoravel_be.domain.location.dto.request.LocationNowPositionDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationMultiReadResponseDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationSingleReadResponseDto;
import trendravel.photoravel_be.domain.location.service.LocationService;
import trendravel.photoravel_be.domain.location.dto.request.LocationRequestDto;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocationServiceTest {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MemberRepository memberRepository;


    @MockBean
    LocationService locationService;
    @MockBean
    ImageServiceFacade imageService;

    LocationRequestDto locationRequestDto;
    Location location;
    Location location2;
    Review review1;
    Review review2;
    Review review3;
    Review review4;
    MemberEntity member;


    @BeforeEach
    void before(){
        locationService = new LocationService(locationRepository, imageService, memberRepository);
        member = MemberEntity.builder()
                .email("asfd")
                .memberId("hwangjeyeon")
                .nickname("hwangs")
                .name("황제연")
                .password("1234")
                .profileImg("1123asd.png")
                .build();
        memberRepository.save(member);

        location = Location
                .builder()
                .name("순천향대학교")
                .latitude(35.24)
                .longitude(46.61)
                .address("아산시 신창면 순천향로46")
                .description("순천향대학교입니다.")
                .views(0)
                .point(new GeometryFactory().createPoint(
                        new Coordinate(35.24
                                , 46.61)))
                .build();
        location.getPoint().setSRID(4326);
        location.setMemberLocation(member);
        location2 = Location
                .builder()
                .name("경찰대학교")
                .latitude(35.22)
                .longitude(46.59)
                .address("아산시 경찰면 경찰로36")
                .description("경찰대학교입니다..")
                .views(0)
                .point(new GeometryFactory().createPoint(
                        new Coordinate(35.22, 46.59)))
                .build();
        location2.getPoint().setSRID(4326);
        location2.setMemberLocation(member);

        review1 = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .content("멋지네")
                .rating(1.5)
                .locationReview(location)
                .member(member)
                .build();
        review2 = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .content("키야")
                .rating(2.4)
                .locationReview(location)
                .member(member)
                .build();
        review3 = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .content("이야")
                .rating(3.42)
                .locationReview(location)
                .member(member)
                .build();
        review4 = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .content("그저 굿")
                .rating(4.5)
                .locationReview(location)
                .member(member)
                .build();
        review1.setLocationReview(location);
        review2.setLocationReview(location);
        review3.setLocationReview(location);
        review4.setLocationReview(location);
        review1.setMemberReview(member);
        review2.setMemberReview(member);
        review3.setMemberReview(member);
        review4.setMemberReview(member);


        locationRequestDto = new LocationRequestDto();
        locationRequestDto.setLocationId(1L);
        locationRequestDto.setName("순천향대학교");
        locationRequestDto.setAddress("아산시 신창면 순천향로46");
        locationRequestDto.setLatitude(35.24);
        locationRequestDto.setLongitude(46.61);
        locationRequestDto.setDescription("순천향대학교입니다.");
        locationRequestDto.setUserId(member.getMemberId());

    }

    @Test
    @DisplayName("Location CREATE (이미지 미포함) 서비스가 잘 작동하는지 테스트")
    @Transactional
    @Order(1)
    void createLocationServiceTest(){

        locationService.createLocation(locationRequestDto);

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
    @Transactional
    @Order(2)
    void updateLocationServiceTest(){
        locationService.createLocation(locationRequestDto);
        locationRequestDto.setLocationId(2L);
        locationRequestDto.setName("미디어랩스");
        locationService.updateLocation(locationRequestDto);

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
    @Transactional
    @Order(5)
    void deleteLocationServiceTest(){
        Long newId = locationService.createLocation(locationRequestDto).getLocationId();
        locationRequestDto.setLocationId(newId);
        locationService.deleteLocation(locationRequestDto.getLocationId());

        assertThat(locationRepository.findById(locationRequestDto.getLocationId())).isEmpty();
    }

    @Test
    @DisplayName("Location SINGLE READ 서비스가 잘 동작하는지 테스트")
    @Transactional
    @Order(3)
    void readSingleLocationServiceTest(){
        //given
        Long id = locationRepository.save(location).getId();
        locationRepository.save(location2);
        locationService.createLocation(locationRequestDto);
        Location findLocation = locationRepository.findById(id).orElse(null);

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);

        //when
        LocationSingleReadResponseDto locationSingleReadResponseDto
                = locationService.readSingleLocation(id);
        List<RecentReviewsDto> findRecentReviews =
                locationSingleReadResponseDto.getRecentReviewDtos();

        //then
        assertThat(locationSingleReadResponseDto.getLatitude())
                .isEqualTo(findLocation.getLatitude());
        assertThat(locationSingleReadResponseDto.getLongitude())
                .isEqualTo(findLocation.getLongitude());
        assertThat(locationSingleReadResponseDto.getDescription())
                .isEqualTo(findLocation.getDescription());
        assertThat(locationSingleReadResponseDto.getAddress())
                .isEqualTo(findLocation.getAddress());
        assertThat(locationSingleReadResponseDto.getRatingAvg())
                .isEqualTo(Double.parseDouble(String.format("%.2f",
                        (review4.getRating() + review2.getRating()
                                + review3.getRating() + review1.getRating()) / 4)));
        assertThat(locationSingleReadResponseDto.getViews()).isEqualTo(1);
        assertThat(findRecentReviews).extracting("rating")
                .containsExactlyInAnyOrder(review4.getRating(),
                        review2.getRating(), review3.getRating());
    }

    @DisplayName("Location MULTI READ (위치기반) 서비스가 잘 동작하는지 테스트")
    @Test
    @Transactional
    @Order(4)
    void readMultiLocationWithPositionServiceTest(){
        locationRepository.save(location);
        locationRepository.save(location2);
        //given
        LocationNowPositionDto locationNowPositionDto
                = new LocationNowPositionDto();
        locationNowPositionDto.setLatitude(35.23);
        locationNowPositionDto.setLongitude(46.60);
        locationNowPositionDto.setRange(2000);
        //when
        List<LocationMultiReadResponseDto> locationMultiReadResponseDto
                = locationService.readMultiLocation(locationNowPositionDto);
        //then

        assertThat(locationMultiReadResponseDto.size()).isEqualTo(2);
        assertThat(locationMultiReadResponseDto.get(0).getLatitude()).isEqualTo(35.24);
        assertThat(locationMultiReadResponseDto.get(0).getLongitude()).isEqualTo(46.61);

        assertThat(locationMultiReadResponseDto.get(1).getLatitude()).isEqualTo(35.22);
        assertThat(locationMultiReadResponseDto.get(1).getLongitude()).isEqualTo(46.59);
    }

    @DisplayName("Location MULTI READ (위치기반 + 검색키워드) 서비스가 잘 동작하는지 테스트")
    @Test
    @Transactional
    @Order(5)
    void readMultiLocationWithSearchServiceTest(){
        //given
        locationRepository.save(location);
        locationRepository.save(location2);
        LocationKeywordDto locationKeywordDto
                = new LocationKeywordDto(35.23, 46.60, 2000, "순천향대");
        //when
        List<LocationMultiReadResponseDto> locationMultiReadResponseDto
                = locationService.readMultiLocation(locationKeywordDto);
        //then

        assertThat(locationMultiReadResponseDto.size()).isEqualTo(1);
        assertThat(locationMultiReadResponseDto.get(0).getLatitude()).isEqualTo(35.24);
        assertThat(locationMultiReadResponseDto.get(0).getLongitude()).isEqualTo(46.61);
        assertThat(locationMultiReadResponseDto.get(0).getName()).isEqualTo("순천향대학교");
    }


    @DisplayName("LOCATION READ의 EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    @Order(6)
    void readExceptionServiceTest(){
        assertThatThrownBy( () -> locationService.readSingleLocation(3L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }

    @DisplayName("LOCATION UPDATE의 EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    @Order(7)
    void updateExceptionServiceTest(){
        locationRequestDto.setLocationId(3L);
        assertThatThrownBy(() -> locationService.updateLocation(locationRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }

    @DisplayName("LOCATION DELETE의 EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    @Order(8)
    void deleteExceptionServiceTest(){

        assertThatThrownBy(() -> locationService.deleteLocation(500L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }

    @DisplayName("LOCATION의 리뷰 수가 100개 이상일 때, 99개로 변경하여 잘 보내는지 테스트")
    @Test
    @Transactional
    @Order(9)
    void locationReviewCountsMoreThanOneHundredTest(){

        for (int i = 0; i < 1000; i++) {
            review1.setLocationReview(location);
            reviewRepository.save(review1);
        }
        Long id = locationRepository.save(location).getId();

        assertThat(locationService.readSingleLocation(id).getReviewCounts()).isEqualTo(99);
    }

    @DisplayName("LOCATION의 리뷰 수가 100개 이하일 때, 그 개수를 그대로 READ하는지 테스트")
    @Test
    @Transactional
    @Order(10)
    void locationReviewCountsLessThanOneHundredTest(){
        Long id = locationRepository.save(location).getId();

        assertThat(locationService.readSingleLocation(id).getReviewCounts())
                .isEqualTo(location.getReview().size());
    }


}