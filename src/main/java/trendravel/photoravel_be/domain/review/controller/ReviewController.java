package trendravel.photoravel_be.domain.review.controller;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.commom.response.Result;
import trendravel.photoravel_be.domain.review.dto.request.ReviewRequestDto;
import trendravel.photoravel_be.domain.review.dto.request.ReviewUpdateImagesDto;
import trendravel.photoravel_be.domain.review.dto.response.ReviewResponseDto;
import trendravel.photoravel_be.domain.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Schema(description = "리뷰 CRUD API 명세서")
public class ReviewController {

    private final ReviewService reviewService;


    @Schema(description = "리뷰 CREATE 요청/응답 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/review/create")
    public Api<ReviewResponseDto> createReview(@RequestBody
                                                   ReviewRequestDto reviewRequestDto){

        return Api.CREATED(reviewService.createReview(reviewRequestDto));
    }

    @Schema(description = "리뷰 CREATE 요청/응답 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/review/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<ReviewResponseDto> createReview(@RequestPart(value = "data")
                                             ReviewRequestDto reviewRequestDto,
                                         @RequestPart(value = "images", required = false)
                                            List<MultipartFile> images){

        return Api.CREATED(reviewService.createReview(reviewRequestDto, images));
    }

    @Schema(description = "특정 장소의 모든 리뷰 READ 요청/응답")
    @GetMapping(value = "/location/{locationId}/detail/reviews")
    public Api<List<ReviewResponseDto>> readLocationReviews(@PathVariable("locationId")
                                                          Long locationId){
        return Api.READ(reviewService.readAllLocationReview(locationId));
    }

    @Schema(description = "특정 스팟의 모든 리뷰 READ 요청/응답")
    @GetMapping(value = "/location/{locationId}/spot/{spotId}/detail/reviews")
    public Api<List<ReviewResponseDto>> readLocationReviews(@PathVariable("locationId")
                                                            Long locationId,
                                                            @PathVariable("spotId")
                                                            Long spotId){
        return Api.READ(reviewService.readAllSpotReview(locationId, spotId));
    }


    @Schema(description = "리뷰 UPDATE 요청 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping(value = "/review/update")
    public Api<ReviewResponseDto> updateReview(@RequestBody
                                               ReviewRequestDto reviewRequestDto){

        return Api.UPDATED(reviewService.updateReview(reviewRequestDto));
    }

    @Schema(description = "리뷰 UPDATE 요청 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PatchMapping(value = "/review/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<ReviewResponseDto> updateReview(@RequestPart(value = "data")
                                                   ReviewUpdateImagesDto reviewRequestDto,
                                         @RequestPart(value = "images", required = false)
                                            List<MultipartFile> images){

        return Api.UPDATED(reviewService.updateReview(reviewRequestDto, images));
    }

    @Schema(description = "리뷰 DELETE 요청")
    @DeleteMapping(value ="/review/{reviewId}/delete")
    public Result locationDelete(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);

        return Result.DELETED();
    }

}
