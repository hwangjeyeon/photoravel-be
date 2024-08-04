package trendravel.photoravel_be.domain.location.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.domain.location.dto.request.LocationRequestDto;
import trendravel.photoravel_be.domain.location.dto.response.LocationResponseDto;
import trendravel.photoravel_be.commom.results.messages.ResultInfo;
import trendravel.photoravel_be.commom.results.OnlyResultDto;
import trendravel.photoravel_be.commom.results.DataResultDto;
import trendravel.photoravel_be.domain.location.service.LocationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final String SUCCESS = "성공";


    @PostMapping(value = "/location/create",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public DataResultDto<LocationResponseDto> locationCreateWithNoImage(@RequestPart(value = "data")
                                           LocationRequestDto locationRequestDto) {

        DataResultDto<LocationResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.CREATED, SUCCESS));
        results.setData(locationService.createLocationWithNoImage(locationRequestDto));
        return results;
    }

    @PostMapping(value = "/location/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResultDto<LocationResponseDto> locationCreate(@RequestPart(value = "data")
                                               LocationRequestDto locationRequestDto,
                                           @RequestPart(value = "images", required = false)
                                                List<MultipartFile> images) {

        DataResultDto<LocationResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.CREATED, SUCCESS));
        results.setData(locationService.createLocation(locationRequestDto, images));
        return results;
    }


    @PatchMapping(value = "/location/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResultDto<?> locationUpdate(@RequestPart(value = "data")
                                               LocationRequestDto locationRequestDto,
                                           @RequestPart(value = "images", required = false)
                                                List<MultipartFile> images) {

        DataResultDto<LocationResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        results.setData(locationService.updateLocation(locationRequestDto, images));
        return results;
    }

    @DeleteMapping("/location/{locationId}/delete")
    public OnlyResultDto locationDelete(@PathVariable("locationId") Long locationId) {
        locationService.deleteLocation(locationId);

        OnlyResultDto results = new OnlyResultDto();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        return results;
    }


}
