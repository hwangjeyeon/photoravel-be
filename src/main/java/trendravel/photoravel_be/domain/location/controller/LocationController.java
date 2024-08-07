package trendravel.photoravel_be.domain.location.controller;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.commom.response.Result;
import trendravel.photoravel_be.domain.location.dto.request.LocationNowPositionDto;
import trendravel.photoravel_be.domain.location.dto.request.LocationRequestDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationMultiReadResponseDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationResponseDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationSingleReadResponseDto;
import trendravel.photoravel_be.domain.location.service.LocationService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @Schema(description = "장소 생성 요청 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/location/create")
    public Api<LocationResponseDto> locationCreate(@RequestBody
                                           LocationRequestDto locationRequestDto) {

        return Api.CREATED(locationService.createLocation(locationRequestDto));
    }

    @Schema(description = "장소 생성 요청 (이미지 포함)",
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


    @GetMapping(value = "/location/{locationId}/detail")
    public Api<LocationSingleReadResponseDto> locationSingleRead(@PathVariable("locationId")
                                                           Long locationId){
        return Api.READ(locationService.readSingleLocation(locationId));
    }

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




    @Schema(description = "장소 수정 요청 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping(value = "/location/update")
    public Api<LocationResponseDto> locationUpdate(@RequestBody
                                                       LocationRequestDto locationRequestDto) {

        return Api.UPDATED(locationService.updateLocation(locationRequestDto));
    }


    @Schema(description = "장소 수정 요청 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PatchMapping(value = "/location/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<LocationResponseDto> locationUpdate(@RequestPart(value = "data")
                                               LocationRequestDto locationRequestDto,
                                           @RequestPart(value = "images", required = false)
                                                List<MultipartFile> images) {

        return Api.UPDATED(locationService.updateLocation(locationRequestDto, images));
    }



    @Schema(description = "장소 삭제 요청")
    @DeleteMapping("/location/{locationId}/delete")
    public Result locationDelete(@PathVariable("locationId") Long locationId) {
        locationService.deleteLocation(locationId);

        return Result.DELETED();
    }


}
