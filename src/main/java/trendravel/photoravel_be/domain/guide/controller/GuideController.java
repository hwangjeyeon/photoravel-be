package trendravel.photoravel_be.domain.guide.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.commom.response.Result;
import trendravel.photoravel_be.domain.guide.dto.request.GuideLoginRequestDto;
import trendravel.photoravel_be.domain.guide.dto.request.GuideRequestDto;
import trendravel.photoravel_be.domain.guide.dto.response.GuideResponseDto;
import trendravel.photoravel_be.domain.guide.service.GuideService;

import java.util.List;

@RestController
@RequestMapping("/guides")
@RequiredArgsConstructor
public class GuideController {
    
    private final GuideService guideService;
    
    
    @GetMapping
    public Api<List<GuideResponseDto>> guidesList(@RequestParam String keyword) {

        return Api.READ(guideService.getGuideList(keyword));
    }
    
    @GetMapping("/{guideId}/detail")
    public Api<GuideResponseDto> guideDetail(@PathVariable String guideId) {

        return Api.READ(guideService.getGuide(guideId));
    }
    
    @PostMapping("/join")
    public Result createGuide(
            @RequestPart(value = "data") GuideRequestDto guideRequestDto,
            @RequestPart(value = "image", required = false) String image) {
        
        guideService.createGuide(guideRequestDto, image);
        
        return Result.CREATED();
    }
    
    @PostMapping("/login")
    public Result guideLogin(@RequestBody GuideLoginRequestDto loginRequestDto) {
        
        guideService.authenticate(loginRequestDto.getUsername(), 
                loginRequestDto.getPassword());
                
        return Result.OK();
    }
    
    @PatchMapping("/{guideId}/update")
    public Api<GuideResponseDto> updateGuide(
            @PathVariable String guideId,
            @RequestPart(value = "data") GuideRequestDto guideRequestDto,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        
        return Api.UPDATED(guideService.updateGuide(guideId, guideRequestDto, images));
    }
    
    @DeleteMapping("/{guideId}/delete")
    public Result guideDelete(@PathVariable String guideId) {
        
        guideService.deleteGuide(guideId);
        return Result.DELETED();
    }
    
}
