package trendravel.photoravel_be.domain.matching.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.commom.response.Result;
import trendravel.photoravel_be.domain.matching.dto.request.MatchingRequestDto;
import trendravel.photoravel_be.domain.matching.dto.response.MatchingResponseDto;
import trendravel.photoravel_be.domain.matching.service.MatchingService;

import java.util.List;

@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchingController {
    
    private final MatchingService matchingService;
    
    @Schema(description = "매칭 PENDING (CREATE) 요청",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/pending")
    Result pendingMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        
        matchingService.pendingMatching(matchingRequestDto);
        return Result.CREATED();
    }
    
    @Schema(description = "매칭 목록 조회",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/{memberId}")
    Api<List<MatchingResponseDto>> getMatching(@PathVariable String memberId) {
        
        return Api.CREATED(matchingService.getMatchingList(memberId));
    }

    @Schema(description = "매칭 취소",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/cancel")
    Result cancelMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        
        matchingService.cancel(matchingRequestDto);
        
        return Result.OK();
    }
    
    @Schema(description = "매칭 수락",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/accept")
    Result acceptMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        
        matchingService.accept(matchingRequestDto);
        
        return Result.CREATED();
    }
    
    @Schema(description = "매칭 거절",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/reject")
    Result rejectMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        
        matchingService.reject(matchingRequestDto);
        
        return Result.CREATED();
    }
    
    @Schema(description = "매칭 종료",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("complete")
    Result completeMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        
        matchingService.complete(matchingRequestDto);
                
        return Result.CREATED();
    }
    
    
}
