package trendravel.photoravel_be.domain.spot.controller;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.commom.response.Result;
import trendravel.photoravel_be.domain.spot.dto.request.SpotRequestDto;
import trendravel.photoravel_be.domain.spot.dto.response.SpotResponseDto;
import trendravel.photoravel_be.db.respository.SpotRepository;
import trendravel.photoravel_be.domain.spot.service.SpotService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;
    private final SpotRepository spotRepository;




    @Schema(description = "스팟 생성 요청 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/spot/create")
    public Api<SpotResponseDto> spotCreate(@RequestBody
                                       SpotRequestDto spotRequestDto){

        return Api.CREATED(spotService.createSpot(spotRequestDto));
    }

    @Schema(description = "스팟 생성 요청 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/spot/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<SpotResponseDto> spotCreate(@RequestPart(value = "data")
                                           SpotRequestDto spotRequestDto,
                                           @RequestPart(value = "images", required = false)
                                           List<MultipartFile> images){

        return Api.CREATED(spotService.createSpot(spotRequestDto, images));
    }


    @Schema(description = "스팟 수정 요청 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping(value = "/spot/update")
    public Api<SpotResponseDto> spotUpdate(@RequestBody
                                       SpotRequestDto spotRequestDto) {

        return Api.UPDATED(spotService.updateSpot(spotRequestDto));
    }

    @Schema(description = "스팟 생성 요청 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PatchMapping(value = "/spot/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<SpotResponseDto> spotUpdate(@RequestPart(value = "data")
                                           SpotRequestDto spotRequestDto,
                                           @RequestPart(value = "images", required = false)
                                           List<MultipartFile> images) {

        return Api.UPDATED(spotService.updateSpot(spotRequestDto, images));
    }

    @Schema(description = "스팟 삭제 요청")
    @DeleteMapping("/spot/{spotId}/delete")
    public Result spotDelete(@PathVariable("spotId") Long spotId) {
        spotRepository.deleteById(spotId);

        return Result.DELETED();
    }


}
