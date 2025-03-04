package trendravel.photoravel_be.common.matching.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import trendravel.photoravel_be.util.response.Api;
import trendravel.photoravel_be.util.response.Result;
import trendravel.photoravel_be.common.matching.dto.request.MatchingRequestDto;
import trendravel.photoravel_be.common.matching.dto.response.MatchingResponseDto;
import trendravel.photoravel_be.common.matching.service.MatchingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MatchingController {
    
    private final MatchingService matchingService;
    
    @Schema(description = "매칭 PENDING (CREATE) 요청",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/private/matching/pending")
    Result pendingMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        
        matchingService.pendingMatching(matchingRequestDto);
        return Result.CREATED();
    }
    
    @Schema(description = "유저의 매칭 목록 조회",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/private/matching/user/{memberId}")
    Api<List<MatchingResponseDto>> getMemberMatching(@PathVariable String memberId) {
        
        return Api.OK(matchingService.getMatchingListByMemberId(memberId));
    }
    
    @Schema(description = "사진작가의 매칭 목록 조회",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/private/matching/photographer/{accountId}")
    Api<List<MatchingResponseDto>> getPhotographerMatching(@PathVariable String accountId) {
        
        return Api.OK(matchingService.getMatchingListByPhotographerId(accountId));
    }

    @Schema(description = "매칭 취소",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/private/matching/cancel")
    Result cancelMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        
        matchingService.cancel(matchingRequestDto);
        
        return Result.OK();
    }
    
    @Schema(description = "매칭 수락",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/private/matching/accept")
    Result acceptMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        
        matchingService.accept(matchingRequestDto);
        
        return Result.OK();
    }
    
    @Schema(description = "매칭 거절",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/private/matching/reject")
    Result rejectMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        
        matchingService.reject(matchingRequestDto);
        
        return Result.OK();
    }
    
    @Schema(description = "매칭 종료",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/private/matching/complete")
    Result completeMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        
        matchingService.complete(matchingRequestDto);
                
        return Result.OK();
    }
    
    
}
