package trendravel.photoravel_be.domain.location.service;



import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.error.LocationErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.location.dto.request.LocationKeywordDto;
import trendravel.photoravel_be.domain.location.dto.request.LocationNowPositionDto;
import trendravel.photoravel_be.domain.location.dto.request.LocationRequestDto;
import trendravel.photoravel_be.domain.location.dto.request.LocationUpdateImagesDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationMultiReadResponseDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationResponseDto;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.domain.location.dto.response.LocationSingleReadResponseDto;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;
import trendravel.photoravel_be.commom.image.service.ImageService;

import java.util.List;


/**
 *  - User 연관관계는 아직 미설정
 */

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final ImageService imageService;

    @Transactional
    public LocationResponseDto createLocation(
            LocationRequestDto locationRequestDto, List<MultipartFile> images) {
        Location location = Location.builder()
                .description(locationRequestDto.getDescription())
                .name(locationRequestDto.getName())
                .latitude(locationRequestDto.getLatitude())
                .longitude(locationRequestDto.getLongitude())
                .images(imageService.uploadImages(images))
                .address(locationRequestDto.getAddress())
                .views(0)
                .point(new GeometryFactory().createPoint(
                                new Coordinate(locationRequestDto.getLatitude()
                                        , locationRequestDto.getLongitude())))
                .build();
        location.getPoint().setSRID(4326);
        locationRepository.save(location);

        return LocationResponseDto
                .builder()
                .LocationId(location.getId())
                .description(location.getDescription())
                .name(location.getName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .images(location.getImages())
                .address(location.getAddress())
                .createdAt(location.getCreatedAt())
                .updatedAt(location.getUpdatedAt())
                .build();
    }

    @Transactional
    public LocationResponseDto createLocation(
            LocationRequestDto locationRequestDto) {
        Location location = Location.builder()
                .description(locationRequestDto.getDescription())
                .name(locationRequestDto.getName())
                .latitude(locationRequestDto.getLatitude())
                .longitude(locationRequestDto.getLongitude())
                .address(locationRequestDto.getAddress())
                .views(0)
                .point(new GeometryFactory().createPoint(
                        new Coordinate(locationRequestDto.getLatitude()
                                , locationRequestDto.getLongitude())))
                .build();
        location.getPoint().setSRID(4326);
        locationRepository.save(location);

        return LocationResponseDto
                .builder()
                .LocationId(location.getId())
                .description(location.getDescription())
                .name(location.getName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .createdAt(location.getCreatedAt())
                .updatedAt(location.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public LocationSingleReadResponseDto readSingleLocation(Long id){
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));


        location.increaseViews();
        List<RecentReviewsDto> reviews = locationRepository.recentReviews(location.getId());

        return LocationSingleReadResponseDto.builder()
                .LocationId(location.getId())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .name(location.getName())
                .description(location.getDescription())
                .updatedAt(location.getUpdatedAt())
                .createdAt(location.getCreatedAt())
                .images(location.getImages())
                .views(location.getViews())
                .ratingAvg(String.format("%.2f", ratingAverage(location.getReview())))
                .recentReviewDtos(reviews)
                .build();
    }

    @Transactional(readOnly = true)
    public List<LocationMultiReadResponseDto> readMultiLocation(
            LocationNowPositionDto locationNowPositionDto){
        List<Location> locations =
                locationRepository.searchNowPosition(locationNowPositionDto);

        return locations.stream()
                .map(p -> new LocationMultiReadResponseDto(
                        p.getId(), p.getLatitude(), p.getLongitude(),
                        p.getAddress(), p.getDescription(), p.getName(),
                        p.getImages(),p.getViews(),
                        String.format("%.2f",ratingAverage(p.getReview())),
                        p.getCreatedAt(), p.getUpdatedAt())
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LocationMultiReadResponseDto> readMultiLocation(
            LocationKeywordDto locationKeywordDto){
        List<Location> locations = locationRepository.searchKeyword(locationKeywordDto);

        return locations.stream()
                .map(p -> new LocationMultiReadResponseDto(
                        p.getId(), p.getLatitude(), p.getLongitude(),
                        p.getAddress(), p.getDescription(), p.getName(),
                        p.getImages(),p.getViews(),
                        String.format("%.2f",ratingAverage(p.getReview())),
                        p.getCreatedAt(), p.getUpdatedAt())
                )
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
    public LocationResponseDto updateLocation(
            LocationUpdateImagesDto locationRequestDto, List<MultipartFile> images) {

        Location location = locationRepository.findById(
                locationRequestDto.getLocationId())
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));


        location.updateLocation(locationRequestDto,
                imageService.updateImages(images, locationRequestDto.getDeleteImages()));


        return LocationResponseDto
                .builder()
                .LocationId(location.getId())
                .description(location.getDescription())
                .name(location.getName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .images(location.getImages())
                .address(location.getAddress())
                .createdAt(location.getCreatedAt())
                .updatedAt(location.getUpdatedAt())
                .build();
    }

    @Transactional
    public LocationResponseDto updateLocation(
            LocationRequestDto locationRequestDto) {

        Location location = locationRepository.findById(
                locationRequestDto.getLocationId())
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));

        location.updateLocation(locationRequestDto);


        return LocationResponseDto
                .builder()
                .LocationId(location.getId())
                .description(location.getDescription())
                .name(location.getName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .createdAt(location.getCreatedAt())
                .updatedAt(location.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteLocation(Long id){
        Location findLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));
        locationRepository.deleteById(findLocation.getId());
        imageService.deleteAllImages(findLocation.getImages());
    }



}
