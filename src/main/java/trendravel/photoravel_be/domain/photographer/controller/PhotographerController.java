package trendravel.photoravel_be.domain.photographer.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.commom.response.Result;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerLoginRequestDto;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerRequestDto;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerResponseDto;
import trendravel.photoravel_be.domain.photographer.service.PhotographerService;

import java.util.List;

@RestController
@RequestMapping("/photographers")
@RequiredArgsConstructor
public class PhotographerController {
    
    private final PhotographerService photographerService;
    
    @Schema(description = "사진작가 회원 가입(CREATE) 요청",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/join",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result createPhotographer(
            @RequestPart(value = "data") PhotographerRequestDto photographerRequestDto,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        
        photographerService.createPhotographer(photographerRequestDto, images);
        
        return Result.CREATED();
    }
    
    @Schema(description = "사진작가 로그인 요청",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/login")
    public Result loginPhotographer(@RequestBody PhotographerLoginRequestDto loginRequestDto) {
        
        photographerService.authenticate(loginRequestDto.getUsername(),
                loginRequestDto.getPassword());
        
        return Result.OK();
    }
    
    @Schema(description = "사진작가 목록 READ 요청",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public Api<List<PhotographerResponseDto>> getPhotographerList(@RequestParam String keyword) {
        
        return Api.READ(photographerService.getPhotographerList(keyword));
    }
    
    @Schema(description = "사진작가 상세 정보 READ 요청")
    @GetMapping("/{photographerId}/detail")
    public Api<PhotographerResponseDto> getPhotographer(@PathVariable String photographerId) {
        
        return Api.READ(photographerService.getPhotographer(photographerId));
    }
    
    
    @Schema(description = "사진작가 정보 UPDATE 요청 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/update")
    public Api<PhotographerResponseDto> updatePhotographer(
            @RequestBody PhotographerRequestDto photographerRequestDto) {
        
        return Api.UPDATED(photographerService.updatePhotographer(photographerRequestDto));
    }
    
    @Schema(description = "사진작가 정보 UPDATE 요청 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PatchMapping(value = "/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<PhotographerResponseDto> updatePhotographer(
            @RequestPart(value = "data") PhotographerRequestDto photographerRequestDto,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        
        return Api.UPDATED(photographerService.updatePhotographer(photographerRequestDto, images));
    }
    
    
    
    @Schema(description = "사진작가 정보 DELETE 요청",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("/{photographerId}/delete")
    public Result deletePhotographer(@PathVariable String photographerId) {
        
        photographerService.deletePhotographer(photographerId);
        return Result.DELETED();
    }
    
}
