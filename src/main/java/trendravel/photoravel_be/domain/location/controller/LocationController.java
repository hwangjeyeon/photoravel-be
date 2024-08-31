package trendravel.photoravel_be.domain.location.controller;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.commom.response.Result;
import trendravel.photoravel_be.domain.location.dto.request.LocationKeywordDto;
import trendravel.photoravel_be.domain.location.dto.request.LocationNowPositionDto;
import trendravel.photoravel_be.domain.location.dto.request.LocationRequestDto;
import trendravel.photoravel_be.domain.location.dto.request.LocationUpdateImagesDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationMultiReadResponseDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationResponseDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationSingleReadResponseDto;
import trendravel.photoravel_be.domain.location.service.LocationService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Schema(description = "장소 CRUD API 명세서")
public class LocationController {

    private final LocationService locationService;

    @Schema(description = "장소 CREATE 요청/응답 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/location/create")
    public Api<LocationResponseDto> locationCreate(@RequestBody
                                           LocationRequestDto locationRequestDto) {

        return Api.CREATED(locationService.createLocation(locationRequestDto));
    }

    @Schema(description = "장소 CREATE 요청/응답 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/location/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<LocationResponseDto> locationCreate(@RequestPart(value = "data")
                                               LocationRequestDto locationRequestDto,
                                           @RequestPart(value = "images", required = false)
                                                List<MultipartFile> images) {

        return Api.CREATED(locationService.createLocation(locationRequestDto, images));
    }

    @Schema(description = "단일 장소 모든 정보 READ 요청/응답")
    @GetMapping(value = "/location/{locationId}/detail")
    public Api<LocationSingleReadResponseDto> locationSingleRead(@PathVariable("locationId")
                                                           Long locationId){
        return Api.READ(locationService.readSingleLocation(locationId));
    }

    @Schema(description = "주변 범위 내 모든 장소 READ 요청/응답")
    @GetMapping(value = "/nowPosition")
    public Api<List<LocationMultiReadResponseDto>> locationMultiRead(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("range") double range){

        return Api.READ(locationService.readMultiLocation(
                new LocationNowPositionDto(
                        latitude, longitude, range
                )
        ));
    }

    @Schema(description = "(주변 범위 + 검색 키워드) 기반 모든 장소 READ 요청/응답")
    @GetMapping(value = "/search/location")
    public Api<List<LocationMultiReadResponseDto>> locationMultiRead(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("range") double range,
            @RequestParam("keyword") String keyword){

        return Api.READ(locationService.readMultiLocation(
                new LocationKeywordDto(
                        latitude, longitude, range, keyword
                )
        ));
    }




    @Schema(description = "장소 UPDATE 요청/응답 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping(value = "/location/update")
    public Api<LocationResponseDto> locationUpdate(@RequestBody
                                                       LocationRequestDto locationRequestDto) {

        return Api.UPDATED(locationService.updateLocation(locationRequestDto));
    }


    @Schema(description = "장소 UPDATE 요청/응답 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PatchMapping(value = "/location/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<LocationResponseDto> locationUpdate(@RequestPart(value = "data")
                                                       LocationUpdateImagesDto locationRequestDto,
                                           @RequestPart(value = "images", required = false)
                                                List<MultipartFile> images) {

        return Api.UPDATED(locationService.updateLocation(locationRequestDto, images));
    }



    @Schema(description = "장소 DELETE 요청/응답")
    @DeleteMapping("/location/{locationId}/delete")
    public Result locationDelete(@PathVariable("locationId") Long locationId) {
        locationService.deleteLocation(locationId);

        return Result.DELETED();
    }


}
