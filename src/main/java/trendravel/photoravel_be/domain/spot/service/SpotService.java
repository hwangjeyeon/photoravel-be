package trendravel.photoravel_be.domain.spot.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.error.LocationErrorCode;
import trendravel.photoravel_be.commom.error.SpotErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.commom.image.service.ImageService;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;
import trendravel.photoravel_be.domain.spot.dto.request.SpotRequestDto;
import trendravel.photoravel_be.domain.spot.dto.request.SpotUpdatedImagesDto;
import trendravel.photoravel_be.domain.spot.dto.response.SpotMultiReadResponseDto;
import trendravel.photoravel_be.domain.spot.dto.response.SpotResponseDto;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.db.respository.spot.SpotRepository;
import trendravel.photoravel_be.domain.spot.dto.response.SpotSingleReadResponseDto;

import java.util.List;

/**
 *  - User 연관관계는 아직 미설정
 */

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final LocationRepository locationRepository;
    private final ImageService imageService;

    @Transactional
    public SpotResponseDto createSpot(
            SpotRequestDto spotRequestDto, List<MultipartFile> images) {

        Location location = locationRepository.findById(spotRequestDto.
                getLocationId())
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));


        Spot spot = Spot.builder()
                .description(spotRequestDto.getDescription())
                .title(spotRequestDto.getTitle())
                .latitude(spotRequestDto.getLatitude())
                .longitude(spotRequestDto.getLongitude())
                .images(imageService.uploadImages(images))
                .location(location)
                .build();
        spot.setLocation(location);
        spotRepository.save(spot);

        return SpotResponseDto
                .builder()
                .spotId(spot.getId())
                .description(spot.getDescription())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .images(spot.getImages())
                .title(spot.getTitle())
                .createdAt(spot.getCreatedAt())
                .updatedAt(spot.getUpdatedAt())
                .build();
    }

    @Transactional
    public SpotResponseDto createSpot(
            SpotRequestDto spotRequestDto) {
        Location location = locationRepository.findById(spotRequestDto.
                getLocationId())
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));


        Spot spot = Spot.builder()
                .description(spotRequestDto.getDescription())
                .title(spotRequestDto.getTitle())
                .latitude(spotRequestDto.getLatitude())
                .longitude(spotRequestDto.getLongitude())
                .location(location)
                .build();
        spot.setLocation(location);
        spotRepository.save(spot);

        return SpotResponseDto
                .builder()
                .spotId(spot.getId())
                .description(spot.getDescription())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .title(spot.getTitle())
                .createdAt(spot.getCreatedAt())
                .updatedAt(spot.getUpdatedAt())
                .build();
    }


    @Transactional(readOnly = true)
    public SpotSingleReadResponseDto readSingleSpot(Long locationId, Long spotId) {
        List<Spot> spots = locationRepository.findById(locationId)
                .map(Location::getSpot)
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));

        Spot spot = spots.stream().
                filter(s -> s.getId().equals(spotId)).findFirst()
                .orElseThrow(() -> new ApiException(SpotErrorCode.SPOT_NOT_FOUND));
        spot.increaseViews();

        List<RecentReviewsDto> reviews = spotRepository.recentReviews(spot.getId());

        return SpotSingleReadResponseDto.builder()
                .spotId(spot.getId())
                .title(spot.getTitle())
                .description(spot.getDescription())
                .createdAt(spot.getCreatedAt())
                .updatedAt(spot.getUpdatedAt())
                .views(spot.getViews())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .images(spot.getImages())
                .ratingAvg(Double.parseDouble
                        (String.format("%.2f", ratingAverage(spot.getReviews()))))
                .recentReviewDtos(reviews)
                .reviewCounts(spot.getReviews().size() < 100 ? spot.getReviews().size(): 99)
                .build();
    }

    @Transactional(readOnly = true)
    public List<SpotMultiReadResponseDto> readMultiSpot(Long locationId) {
        List<Spot> spots = locationRepository.findById(locationId)
                .map(Location::getSpot)
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));

        return spots.stream()
                .map(p -> new SpotMultiReadResponseDto(
                        p.getId(), p.getTitle(), p.getDescription(),
                        p.getLatitude(),p.getLongitude(),
                        p.getImages(), p.getViews(),
                        p.getCreatedAt(), p.getUpdatedAt()
                ))
                .toList();
    }

    private double ratingAverage(List<Review> reviews) {
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }



    @Transactional
    public SpotResponseDto updateSpot(
            SpotUpdatedImagesDto spotRequestDto, List<MultipartFile> images) {

        Spot spot = spotRepository.findById(
                spotRequestDto.getSpotId())
                .orElseThrow(() -> new ApiException(SpotErrorCode.SPOT_NOT_FOUND));

        spot.updateSpot(spotRequestDto, imageService.updateImages(
                images, spotRequestDto.getDeleteImages()));

        return SpotResponseDto
                .builder()
                .spotId(spot.getId())
                .title(spot.getTitle())
                .description(spot.getDescription())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .images(spot.getImages())
                .createdAt(spot.getCreatedAt())
                .updatedAt(spot.getUpdatedAt())
                .build();
    }

    @Transactional
    public SpotResponseDto updateSpot(
            SpotRequestDto spotRequestDto) {

        Spot spot = spotRepository.findById(
                spotRequestDto.getSpotId())
                .orElseThrow(() -> new ApiException(SpotErrorCode.SPOT_NOT_FOUND));

        spot.updateSpot(spotRequestDto);

        return SpotResponseDto
                .builder()
                .spotId(spot.getId())
                .title(spot.getTitle())
                .description(spot.getDescription())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .createdAt(spot.getCreatedAt())
                .updatedAt(spot.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteSpot(Long id){
        Spot findSpot = spotRepository.findById(id)
                .orElseThrow(() -> new ApiException(SpotErrorCode.SPOT_NOT_FOUND));
        spotRepository.deleteById(findSpot.getId());
        imageService.deleteAllImages(findSpot.getImages());
    }


}
