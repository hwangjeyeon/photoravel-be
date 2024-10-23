package trendravel.photoravel_be.domain.spot.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "단일 스팟 READ 응답 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class SpotSingleReadResponseDto {

    @Schema(description = "스팟 ID")
    private Long spotId;
    @Schema(description = "스팟 제목")
    private String title;
    @Schema(description = "스팟 내용")
    private String description;
    @Schema(description = "스팟 위도")
    private double latitude;
    @Schema(description = "스팟 경도")
    private double longitude;
    @Schema(description = "스팟 이미지들")
    private List<String> images;

    @Schema(description = "단일 스팟 조회수")
    private int views;

    @Schema(description = "단일 스팟의 모든 리뷰 평균 평점")
    private Double ratingAvg;

    @Schema(description = "장소 리뷰 수")
    private Integer reviewCounts;

    @Schema(description = "스팟 작성 유저")
    private String userName;

    @Schema(description = "최근 업데이트된 리뷰 목록 (최대 3개)")
    private List<RecentReviewsDto> recentReviewDtos;

    @Schema(description = "스팟 생성일")
    private LocalDateTime createdAt;
    @Schema(description = "스팟 수정일")
    private LocalDateTime updatedAt;

}
