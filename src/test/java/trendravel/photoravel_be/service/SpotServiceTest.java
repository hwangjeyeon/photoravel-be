package trendravel.photoravel_be.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import trendravel.photoravel_be.commom.service.ImageService;
import trendravel.photoravel_be.domain.spot.service.SpotService;
import trendravel.photoravel_be.domain.spot.dto.request.SpotRequestDto;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.db.respository.spot.SpotRepository;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpotServiceTest {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    SpotRepository spotRepository;

    @MockBean
    SpotService spotService;

    @MockBean
    ImageService imageService;

    SpotRequestDto spotRequestDto;
    Location location;

    @BeforeEach
    void before(){
        spotService = new SpotService(spotRepository, locationRepository, imageService);
        spotRequestDto = new SpotRequestDto();
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

        spotRequestDto.setSpotId(1L);
        spotRequestDto.setTitle("미디어랩스건물 방문");
        spotRequestDto.setDescription("미디어랩스관입니다");
        spotRequestDto.setLatitude(46.61);
        spotRequestDto.setLongitude(35.24);
        spotRequestDto.setLocationId(locationRepository.findById(1L).get().getId());
    }

    @Order(1)
    @Test
    @DisplayName("Spot CREATE 서비스가 (이미지 미포함) 잘 작동하는지 테스트")
    void createLocationServiceTest(){
        spotService.createSpot(spotRequestDto);

        assertThat(spotRepository.findById(
                        spotRequestDto.getSpotId())
                .get().getId()).isEqualTo(1L);
        assertThat(spotRepository.findById(
                spotRequestDto.getSpotId())
                .get().getLocation().getId()).
                isEqualTo(locationRepository.findById(1L).get().getId());
        assertThat(spotRepository.findById(
                        spotRequestDto.getSpotId())
                .get().getTitle()).isEqualTo("미디어랩스건물 방문");
        assertThat(spotRepository.findById(
                        spotRequestDto.getSpotId())
                .get().getLatitude()).isEqualTo(46.61);
        assertThat(spotRepository.findById(
                        spotRequestDto.getSpotId())
                .get().getLongitude()).isEqualTo(35.24);
        assertThat(spotRepository.findById(
                        spotRequestDto.getSpotId())
                .get().getDescription()).isEqualTo("미디어랩스관입니다");

    }

    @Order(2)
    @Test
    @DisplayName("Spot UPDATE 서비스가 (이미지 미포함) 잘 작동하는지 테스트")
    void updateLocationServiceTest(){
        spotService.createSpot(spotRequestDto);
        spotRequestDto.setTitle("미디어랩스 방문 후 모습");
        spotService.updateSpot(spotRequestDto);

        assertThat(spotRepository.findById(
                        spotRequestDto.getLocationId())
                .get().getId()).isEqualTo(1L);
        assertThat(spotRepository.findById(
                        spotRequestDto.getLocationId())
                .get().getTitle()).isEqualTo("미디어랩스 방문 후 모습");
        assertThat(spotRepository.findById(
                        spotRequestDto.getLocationId())
                .get().getLatitude()).isEqualTo(46.61);
        assertThat(spotRepository.findById(
                        spotRequestDto.getLocationId())
                .get().getLongitude()).isEqualTo(35.24);
        assertThat(spotRepository.findById(
                        spotRequestDto.getLocationId())
                .get().getDescription()).isEqualTo("미디어랩스관입니다");
    }

    @Order(3)
    @Test
    @DisplayName("Spot DELETE 서비스가 잘 작동하는지 테스트")
    void deleteLocationServiceTest(){
        spotService.createSpot(spotRequestDto);
        spotService.deleteSpot(spotRequestDto.getSpotId());

        assertThat(spotRepository.findById(spotRequestDto.getSpotId())).isEmpty();
    }

}