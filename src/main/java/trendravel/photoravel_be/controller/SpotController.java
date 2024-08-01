package trendravel.photoravel_be.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trendravel.photoravel_be.dto.request.SpotRequestDto;
import trendravel.photoravel_be.dto.response.domain.SpotResponseDto;
import trendravel.photoravel_be.dto.response.messages.ResultInfo;
import trendravel.photoravel_be.dto.response.results.DataResultDto;
import trendravel.photoravel_be.dto.response.results.OnlyResultDto;
import trendravel.photoravel_be.repository.SpotRepository;
import trendravel.photoravel_be.service.SpotService;

@RestController
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;
    private final String SUCCESS = "성공";
    private final SpotRepository spotRepository;

    @PostMapping("/spot/create")
    public DataResultDto<?> spotCreate(@RequestBody SpotRequestDto spotRequestDto) {
        DataResultDto<SpotResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.CREATED, SUCCESS));
        results.setData(spotService.createSpot(spotRequestDto));
        return results;
    }


    @PatchMapping("/spot/update")
    public DataResultDto<?> spotUpdate(@RequestBody SpotRequestDto spotRequestDto) {
        DataResultDto<SpotResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        results.setData(spotService.updateSpot(spotRequestDto));
        return results;
    }

    @DeleteMapping("/location/{locationId}")
    public OnlyResultDto locationDelete(@PathVariable Long locationId) {
        spotRepository.deleteById(locationId);

        OnlyResultDto results = new OnlyResultDto();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        return results;
    }


}
