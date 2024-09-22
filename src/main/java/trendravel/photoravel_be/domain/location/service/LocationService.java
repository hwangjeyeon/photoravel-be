package trendravel.photoravel_be.domain.location.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.error.LocationErrorCode;
import trendravel.photoravel_be.commom.error.MemberErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.commom.image.service.ImageServiceFacade;
import trendravel.photoravel_be.db.enums.Category;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.spot.Spot;
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


import java.util.ArrayList;
import java.util.List;


/**
 *  - User 연관관계는 아직 미설정
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final LocationRepository locationRepository;
    private final ImageServiceFacade imageServiceFacade;
    private final MemberRepository memberRepository;

    @Transactional
    public LocationResponseDto createLocation(
            LocationRequestDto locationRequestDto, List<MultipartFile> images) {
        MemberEntity member = memberRepository.findByMemberId(locationRequestDto.getUserId())
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));

        Location location = Location.builder()
                .description(locationRequestDto.getDescription())
                .name(locationRequestDto.getName())
                .images(imageServiceFacade.uploadImageFacade(images))
                .latitude(locationRequestDto.getLatitude())
                .longitude(locationRequestDto.getLongitude())
                .address(locationRequestDto.getAddress())
                .views(0)
                .point(new GeometryFactory().createPoint(
                                new Coordinate(locationRequestDto.getLatitude()
                                        , locationRequestDto.getLongitude())))
                .member(member)
                .category(locationRequestDto.getCategory() == null
                        ? Category.None : locationRequestDto.getCategory())
                .build();
        location.getPoint().setSRID(4326);
        location.setMemberLocation(member);
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
                .userName(location.getMember().getNickname())
                .category(location.getCategory().getMessage())
                .build();
    }

    @Transactional
    public LocationResponseDto createLocation(
            LocationRequestDto locationRequestDto) {
        MemberEntity member = memberRepository.findByMemberId(locationRequestDto.getUserId())
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));
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
                .member(member)
                .category(locationRequestDto.getCategory() == null
                        ? Category.None : locationRequestDto.getCategory())
                .build();
        location.getPoint().setSRID(4326);
        location.setMemberLocation(member);
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
                .userName(location.getMember().getNickname())
                .category(location.getCategory().getMessage())
                .build();
    }

    @Transactional(readOnly = true)
    public LocationSingleReadResponseDto readSingleLocation(Long id){
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));

        MemberEntity member = memberRepository.findByMemberId(location.getMember().getMemberId())
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));

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
                .ratingAvg(Double.parseDouble(String.format("%.2f",
                        ratingAverage(location.getReview()))))
                .reviewCounts(location.getReview().size() < 100
                        ? location.getReview().size() : 99)
                .recentReviewDtos(reviews)
                .userName(member.getNickname())
                .category(location.getCategory().getMessage())
                .build();
    }

    @Transactional(readOnly = true)
    public List<LocationMultiReadResponseDto> readMultiLocation(
            LocationNowPositionDto locationNowPositionDto){
        List<Location> locations =
                locationRepository.searchNowPosition(locationNowPositionDto);

        return getLocationResponseLists(locations);
    }

    @Transactional(readOnly = true)
    public List<LocationMultiReadResponseDto> readMultiLocation(
            LocationKeywordDto locationKeywordDto){
        List<Location> locations = locationRepository.searchKeyword(locationKeywordDto);

        return getLocationResponseLists(locations);
    }


    private double ratingAverage(List<Review> reviews) {
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }

    @NotNull
    private List<LocationMultiReadResponseDto> getLocationResponseLists(List<Location> locations) {
        return locations.stream()
                .map(p -> new LocationMultiReadResponseDto(
                        p.getId(), p.getLatitude(), p.getLongitude(),
                        p.getAddress(), p.getDescription(), p.getName(),
                        p.getImages(),p.getViews(),
                        Double.parseDouble(String.format("%.2f",
                                ratingAverage(p.getReview()))),
                        p.getReview().size() < 100 ? p.getReview().size() : 99,
                        p.getCreatedAt(), p.getUpdatedAt(), p.getCategory().getMessage())
                )
                .toList();
    }



    @Transactional
    public LocationResponseDto updateLocation(
            LocationUpdateImagesDto locationRequestDto,
            List<MultipartFile> images) {

        Location location = locationRepository.findById(
                locationRequestDto.getLocationId())
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));

        log.info("이미지 저장 전");
        location.updateLocation(locationRequestDto,
                imageServiceFacade.updateImageFacade(images, locationRequestDto.getDeleteImages()));
        log.info("이미지 저장 후");

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
                .userName(location.getMember().getNickname())
                .category(location.getCategory().getMessage())
                .build();
    }

    @Transactional
    public LocationResponseDto updateLocation(
            LocationUpdateImagesDto locationRequestDto) {

        Location location = locationRepository.findById(
                locationRequestDto.getLocationId())
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));


        location.updateLocation(locationRequestDto);
        imageServiceFacade.updateImageFacade(new ArrayList<>(), locationRequestDto.getDeleteImages());


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
                .userName(location.getMember().getNickname())
                .images(location.getImages())
                .category(location.getCategory().getMessage())
                .build();
    }

    @Transactional
    public void deleteLocation(Long id){
        Location findLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));
        imageServiceFacade.deleteAllImagesFacade(deleteSubDomainImages(findLocation));
        locationRepository.deleteById(findLocation.getId());
    }


    private List<String> deleteSubDomainImages(Location location){
        List<String> deleteImages = new ArrayList<>();
        if(location.getImages() != null){
            deleteImages.addAll(location.getImages());
        }

        for (Spot spot : location.getSpot()) {
            if(spot.getImages() == null){
                continue;
            }
            deleteImages.addAll(spot.getImages());
        }
        for (Review review : location.getReview()) {
            if(review.getImages() == null){
                continue;
            }
            deleteImages.addAll(review.getImages());
        }
        return deleteImages;
    }

}
