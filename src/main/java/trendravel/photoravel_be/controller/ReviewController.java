package trendravel.photoravel_be.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.dto.request.ReviewRequestDto;
import trendravel.photoravel_be.dto.response.domain.ReviewResponseDto;
import trendravel.photoravel_be.dto.response.messages.ResultInfo;
import trendravel.photoravel_be.dto.response.results.DataResultDto;
import trendravel.photoravel_be.dto.response.results.OnlyResultDto;
import trendravel.photoravel_be.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final String SUCCESS = "성공";

    @PostMapping("/review/create")
    public DataResultDto<?> createReview(@RequestPart(value = "data")
                                             ReviewRequestDto reviewRequestDto,
                                         @RequestPart(value = "images", required = false)
                                            List<MultipartFile> images){
        DataResultDto<ReviewResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.CREATED, SUCCESS));
        results.setData(reviewService.createReview(reviewRequestDto, images));
        return results;
    }

    @PatchMapping("/review/update")
    public DataResultDto<?> updateReview(@RequestPart(value = "data")
                                             ReviewRequestDto reviewRequestDto,
                                         @RequestPart(value = "images", required = false)
                                            List<MultipartFile> images){
        DataResultDto<ReviewResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        results.setData(reviewService.updateReview(reviewRequestDto, images));
        return results;
    }

    @DeleteMapping("/review/{reviewId}/delete")
    public OnlyResultDto locationDelete(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);

        OnlyResultDto results = new OnlyResultDto();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        return results;
    }

}
