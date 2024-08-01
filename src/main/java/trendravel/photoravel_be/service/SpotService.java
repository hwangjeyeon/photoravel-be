package trendravel.photoravel_be.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trendravel.photoravel_be.dto.request.SpotRequestDto;
import trendravel.photoravel_be.dto.response.domain.SpotResponseDto;
import trendravel.photoravel_be.entity.Spot;
import trendravel.photoravel_be.repository.LocationRepository;
import trendravel.photoravel_be.repository.SpotRepository;

import java.util.Optional;

/**
 *  - User 연관관계는 아직 미설정
 */

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final LocationRepository locationRepository;

    public SpotResponseDto createSpot(
            SpotRequestDto spotRequestDto) {
        Spot spot = spotRepository.save(Spot.builder()
                .description(spotRequestDto.getDescription())
                .latitude(spotRequestDto.getLatitude())
                .longitude(spotRequestDto.getLongitude())
                .images(spotRequestDto.getImages())
                .location(locationRepository.findById(spotRequestDto.getLocationId()).get())
                .build());

        return SpotResponseDto
                .builder()
                .spotId(spot.getId())
                .description(spot.getDescription())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .images(spot.getImages())
                .createdTime(spot.getCreatedTime())
                .updatedTime(spot.getUpdatedTime())
                .build();
    }


    @Transactional
    public SpotResponseDto updateSpot(
            SpotRequestDto spotRequestDto) {

        Optional<Spot> spot = spotRepository.findById(
                spotRequestDto.getSpotId());

        if(spot.isEmpty()){
            // 추후 Exception Controller 만들어 처리할 계획
        }
        spot.get().updateSpot(spotRequestDto);

        return SpotResponseDto
                .builder()
                .spotId(spot.get().getId())
                .description(spot.get().getDescription())
                .latitude(spot.get().getLatitude())
                .longitude(spot.get().getLongitude())
                .images(spot.get().getImages())
                .createdTime(spot.get().getCreatedTime())
                .updatedTime(spot.get().getUpdatedTime())
                .build();
    }

    public void deleteLocation(Long id){
        spotRepository.deleteById(id);
    }


}
