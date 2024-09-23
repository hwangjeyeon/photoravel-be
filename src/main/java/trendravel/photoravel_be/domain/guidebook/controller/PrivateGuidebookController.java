package trendravel.photoravel_be.domain.guidebook.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.commom.response.Result;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookRequestDto;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookUpdateDto;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookUpdateImageDto;
import trendravel.photoravel_be.domain.guidebook.dto.response.GuidebookResponseDto;
import trendravel.photoravel_be.domain.guidebook.service.GuidebookService;

import java.util.List;

@RestController
@RequestMapping("/private/guidebooks")
@RequiredArgsConstructor
public class PrivateGuidebookController {
    
    private final GuidebookService guidebookService;
    
    @Schema(description = "가이드북 CREATE 요청 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/create")
    public Api<GuidebookResponseDto> guidebookCreate(
            @RequestBody GuidebookRequestDto guidebookRequestDto) {
        
        return Api.CREATED(guidebookService.createGuidebook(guidebookRequestDto));
    }
    
    @Schema(description = "가이드북 CREATE 요청 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<GuidebookResponseDto> guidebookCreate(
            @RequestPart(value = "data") GuidebookRequestDto guidebookRequestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        
        return Api.CREATED(guidebookService.createGuidebook(guidebookRequestDto, images));
    }
    
    @Schema(description = "가이드북 UPDATE 요청 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/update")
    public Api<GuidebookResponseDto> updateGuidebook(
            @RequestBody GuidebookUpdateDto guidebookUpdateDto) {
        
        return Api.UPDATED(guidebookService.updateGuidebook(guidebookUpdateDto));
    }
    
    @Schema(description = "가이드북 UPDATE 요청 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PatchMapping(value = "/private/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<GuidebookResponseDto> updateGuidebook(
            @RequestPart(value = "data") GuidebookUpdateImageDto guidebookUpdateImageDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        
        return Api.UPDATED(guidebookService.updateGuidebook(guidebookUpdateImageDto, images));
    }
    
    @Schema(description = "가이드북 DELETE 요청")
    @DeleteMapping("/{guidebookId}/delete")
    public Result guidebookDelete(@PathVariable Long guidebookId) {
        
        guidebookService.deleteGuidebook(guidebookId);
        return Result.DELETED();
    }
    
}
