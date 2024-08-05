package trendravel.photoravel_be.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import trendravel.photoravel_be.commom.service.ImageService;
import trendravel.photoravel_be.domain.location.service.LocationService;
import trendravel.photoravel_be.domain.location.dto.request.LocationRequestDto;
import trendravel.photoravel_be.db.respository.location.LocationRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LocationServiceTest {

    @Autowired
    LocationRepository locationRepository;
    @MockBean
    LocationService locationService;
    @MockBean
    ImageService imageService;

    LocationRequestDto locationRequestDto;
    List<String> image = new ArrayList<>();

    @BeforeEach
    void before(){
        locationService = new LocationService(locationRepository, imageService);
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


}