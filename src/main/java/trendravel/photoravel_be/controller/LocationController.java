package trendravel.photoravel_be.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trendravel.photoravel_be.dto.request.LocationRequestDto;
import trendravel.photoravel_be.dto.response.domain.LocationResponseDto;
import trendravel.photoravel_be.dto.response.messages.ResultInfo;
import trendravel.photoravel_be.dto.response.results.OnlyResultDto;
import trendravel.photoravel_be.dto.response.results.DataResultDto;
import trendravel.photoravel_be.service.LocationService;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final String SUCCESS = "성공";

    @PostMapping("/location/create")
    public DataResultDto<?> locationCreate(@RequestBody LocationRequestDto locationRequestDto) {

        DataResultDto<LocationResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.CREATED, SUCCESS));
        results.setData(locationService.createLocation(locationRequestDto));
        return results;
    }


    @PatchMapping("/location/update")
    public DataResultDto<?> locationUpdate(@RequestBody LocationRequestDto locationRequestDto) {

        DataResultDto<LocationResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        results.setData(locationService.updateLocation(locationRequestDto));
        return results;
    }

    @DeleteMapping("/location/{locationId}")
    public OnlyResultDto locationDelete(@PathVariable Long locationId) {
        locationService.deleteLocation(locationId);

        OnlyResultDto results = new OnlyResultDto();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        return results;
    }


}
