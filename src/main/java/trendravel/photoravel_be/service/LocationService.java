package trendravel.photoravel_be.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trendravel.photoravel_be.dto.request.LocationRequestDto;
import trendravel.photoravel_be.dto.response.domain.LocationResponseDto;
import trendravel.photoravel_be.entity.Location;
import trendravel.photoravel_be.repository.LocationRepository;

import java.util.Optional;


/**
 *  - User 연관관계는 아직 미설정
 */

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;


    public LocationResponseDto createLocation(
            LocationRequestDto locationRequestDto) {
        Location location = locationRepository.save(Location.builder()
                .description(locationRequestDto.getDescription())
                .name(locationRequestDto.getName())
                .latitude(locationRequestDto.getLatitude())
                .longitude(locationRequestDto.getLongitude())
                .images(locationRequestDto.getImages())
                .address(locationRequestDto.getAddress())
                .views(0)
                .build());

        return LocationResponseDto
                .builder()
                .LocationId(location.getId())
                .description(location.getDescription())
                .name(location.getName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .images(location.getImages())
                .address(location.getAddress())
                .createdTime(location.getCreatedTime())
                .updatedTime(location.getUpdatedTime())
                .build();
    }


    @Transactional
    public LocationResponseDto updateLocation(
            LocationRequestDto locationRequestDto) {

        Optional<Location> location = locationRepository.findById(
                locationRequestDto.getLocationId());

        if(location.isEmpty()){
            // 추후 Exception Controller 만들어 처리할 계획
        }
        location.get().updateLocation(locationRequestDto);


        return LocationResponseDto
                .builder()
                .LocationId(location.get().getId())
                .description(location.get().getDescription())
                .name(location.get().getName())
                .latitude(location.get().getLatitude())
                .longitude(location.get().getLongitude())
                .images(location.get().getImages())
                .address(location.get().getAddress())
                .createdTime(location.get().getCreatedTime())
                .updatedTime(location.get().getUpdatedTime())
                .build();
    }

    public void deleteLocation(Long id){
        locationRepository.deleteById(id);
    }



}
