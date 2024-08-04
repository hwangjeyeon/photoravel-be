package trendravel.photoravel_be.domain.spot.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.domain.spot.dto.request.SpotRequestDto;
import trendravel.photoravel_be.domain.spot.dto.response.SpotResponseDto;
import trendravel.photoravel_be.commom.results.messages.ResultInfo;
import trendravel.photoravel_be.commom.results.DataResultDto;
import trendravel.photoravel_be.commom.results.OnlyResultDto;
import trendravel.photoravel_be.db.respository.SpotRepository;
import trendravel.photoravel_be.domain.spot.service.SpotService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;
    private final String SUCCESS = "성공";
    private final SpotRepository spotRepository;

    @PostMapping(value = "/spot/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResultDto<SpotResponseDto> spotCreate(@RequestPart(value = "data")
                                           SpotRequestDto spotRequestDto,
                                       @RequestPart(value = "images", required = false)
                                            List<MultipartFile> images){
        DataResultDto<SpotResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.CREATED, SUCCESS));
        results.setData(spotService.createSpot(spotRequestDto, images));
        return results;
    }

    @PostMapping(value = "/spot/create")
    public DataResultDto<SpotResponseDto> spotCreate(@RequestBody
                                       SpotRequestDto spotRequestDto){
        DataResultDto<SpotResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.CREATED, SUCCESS));
        results.setData(spotService.createSpot(spotRequestDto));
        return results;
    }


    @PatchMapping(value = "/spot/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResultDto<SpotResponseDto> spotUpdate(@RequestPart(value = "data")
                                           SpotRequestDto spotRequestDto,
                                       @RequestPart(value = "images", required = false)
                                            List<MultipartFile> images) {
        DataResultDto<SpotResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        results.setData(spotService.updateSpot(spotRequestDto, images));
        return results;
    }

    @PatchMapping(value = "/spot/update")
    public DataResultDto<SpotResponseDto> spotUpdate(@RequestBody
                                       SpotRequestDto spotRequestDto) {
        DataResultDto<SpotResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        results.setData(spotService.updateSpot(spotRequestDto));
        return results;
    }

    @DeleteMapping("/spot/{spotId}/delete")
    public OnlyResultDto spotDelete(@PathVariable("spotId") Long spotId) {
        spotRepository.deleteById(spotId);

        OnlyResultDto results = new OnlyResultDto();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        return results;
    }


}
