package trendravel.photoravel_be.domain.spot.controller;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.commom.response.Result;
import trendravel.photoravel_be.domain.spot.dto.request.SpotRequestDto;
import trendravel.photoravel_be.domain.spot.dto.request.SpotUpdatedImagesDto;
import trendravel.photoravel_be.domain.spot.dto.response.SpotMultiReadResponseDto;
import trendravel.photoravel_be.domain.spot.dto.response.SpotResponseDto;
import trendravel.photoravel_be.domain.spot.dto.response.SpotSingleReadResponseDto;
import trendravel.photoravel_be.domain.spot.service.SpotService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Schema(description = "스팟 CRUD API 명세서")
public class SpotController {

    private final SpotService spotService;



    @Schema(description = "스팟 CREATE 요청/응답 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/private/spot/create")
    public Api<SpotResponseDto> spotCreate(@RequestBody
                                       SpotRequestDto spotRequestDto){

        return Api.CREATED(spotService.createSpot(spotRequestDto));
    }

    @Schema(description = "스팟 CREATE 요청/응답 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/private/spot/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<SpotResponseDto> spotCreate(@RequestPart(value = "data")
                                           SpotRequestDto spotRequestDto,
                                           @RequestPart(value = "images", required = false)
                                           List<MultipartFile> images){

        return Api.CREATED(spotService.createSpot(spotRequestDto, images));
    }

    @Schema(description = "단일 스팟 모든 정보 READ 요청/응답")
    @GetMapping(value = "/location/{locationSearchId}/spot/{spotSearchId}/detail")
    public Api<SpotSingleReadResponseDto> spotSingleRead(@PathVariable("locationSearchId")
                                                Long locationSearchId,
                                                         @PathVariable("spotSearchId")
                                                Long spotSearchId){
        return Api.READ(spotService.readSingleSpot(locationSearchId, spotSearchId));
    }

    @Schema(description = "특정 장소의 모든 스팟 정보 READ 교청/응답")
    @GetMapping(value = "/location/{locationSearchId}/spots")
    public Api<List<SpotMultiReadResponseDto>> spotMultiRead(@PathVariable("locationSearchId")
                                                       Long locationSearchId){
        return Api.READ(spotService.readMultiSpot(locationSearchId));
    }


    @Schema(description = "스팟 UPDATE 요청/응답 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping(value = "/private/spot/update")
    public Api<SpotResponseDto> spotUpdate(@RequestBody
                                       SpotRequestDto spotRequestDto) {

        return Api.UPDATED(spotService.updateSpot(spotRequestDto));
    }

    @Schema(description = "스팟 UPDATE 요청/응답 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PatchMapping(value = "/private/spot/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<SpotResponseDto> spotUpdate(@RequestPart(value = "data")
                                               SpotUpdatedImagesDto spotRequestDto,
                                           @RequestPart(value = "images", required = false)
                                           List<MultipartFile> images) {

        return Api.UPDATED(spotService.updateSpot(spotRequestDto, images));
    }

    @Schema(description = "스팟 DELETE 요청")
    @DeleteMapping("/private/spot/{spotId}/delete")
    public Result spotDelete(@PathVariable("spotId") Long spotId) {
        spotService.deleteSpot(spotId);

        return Result.DELETED();
    }


}
