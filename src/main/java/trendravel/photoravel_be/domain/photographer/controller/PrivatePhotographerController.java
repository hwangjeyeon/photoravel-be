package trendravel.photoravel_be.domain.photographer.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.commom.response.Result;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerUpdateDto;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerSingleResponseDto;
import trendravel.photoravel_be.domain.photographer.service.PhotographerService;

import java.util.List;

@RestController
@RequestMapping("/private/photographers")
@RequiredArgsConstructor
public class PrivatePhotographerController {
    
    private final PhotographerService photographerService;
    

    
    
    @Schema(description = "사진작가 정보 UPDATE 요청 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/update")
    public Api<PhotographerSingleResponseDto> updatePhotographer(
            @RequestBody PhotographerUpdateDto photographerUpdateDto) {
        
        return Api.UPDATED(photographerService.updatePhotographer(photographerUpdateDto));
    }
    
    @Schema(description = "사진작가 정보 UPDATE 요청 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PatchMapping(value = "/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<PhotographerSingleResponseDto> updatePhotographer(
            @RequestPart(value = "data") PhotographerUpdateDto photographerUpdateDto,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        
        return Api.UPDATED(photographerService.updatePhotographer(photographerUpdateDto, images));
    }
    
    
    
    @Schema(description = "사진작가 정보 DELETE 요청",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("/{photographerId}/delete")
    public Result deletePhotographer(@PathVariable String photographerId) {
        
        photographerService.deletePhotographer(photographerId);
        return Result.DELETED();
    }
    
}
