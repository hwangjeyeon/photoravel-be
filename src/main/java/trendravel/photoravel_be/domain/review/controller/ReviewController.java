package trendravel.photoravel_be.domain.review.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.domain.review.dto.request.ReviewRequestDto;
import trendravel.photoravel_be.domain.review.dto.response.ReviewResponseDto;
import trendravel.photoravel_be.commom.results.messages.ResultInfo;
import trendravel.photoravel_be.commom.results.DataResultDto;
import trendravel.photoravel_be.commom.results.OnlyResultDto;
import trendravel.photoravel_be.domain.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final String SUCCESS = "성공";

    @PostMapping(value = "/review/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResultDto<ReviewResponseDto> createReview(@RequestPart(value = "data")
                                             ReviewRequestDto reviewRequestDto,
                                         @RequestPart(value = "images", required = false)
                                            List<MultipartFile> images){
        DataResultDto<ReviewResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.CREATED, SUCCESS));
        results.setData(reviewService.createReview(reviewRequestDto, images));
        return results;
    }

    @PostMapping(value = "/review/create")
    public DataResultDto<ReviewResponseDto> createReview(@RequestBody
                                         ReviewRequestDto reviewRequestDto){
        DataResultDto<ReviewResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.CREATED, SUCCESS));
        results.setData(reviewService.createReview(reviewRequestDto));
        return results;
    }

    @PatchMapping(value = "/review/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResultDto<ReviewResponseDto> updateReview(@RequestPart(value = "data")
                                             ReviewRequestDto reviewRequestDto,
                                         @RequestPart(value = "images", required = false)
                                            List<MultipartFile> images){
        DataResultDto<ReviewResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        results.setData(reviewService.updateReview(reviewRequestDto, images));
        return results;
    }

    @PatchMapping(value = "/review/update")
    public DataResultDto<ReviewResponseDto> updateReview(@RequestBody
                                         ReviewRequestDto reviewRequestDto){
        DataResultDto<ReviewResponseDto> results = new DataResultDto<>();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        results.setData(reviewService.updateReview(reviewRequestDto));
        return results;
    }

    @DeleteMapping(value ="/review/{reviewId}/delete")
    public OnlyResultDto locationDelete(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);

        OnlyResultDto results = new OnlyResultDto();
        results.setResult(new ResultInfo(HttpStatus.OK, SUCCESS));
        return results;
    }

}
