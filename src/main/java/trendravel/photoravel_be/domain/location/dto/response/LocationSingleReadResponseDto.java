package trendravel.photoravel_be.domain.location.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.enums.Category;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "단일 장소 READ 응답 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class LocationSingleReadResponseDto {

    @Schema(description = "장소ID")
    private Long LocationId;
    @Schema(description = "위도")
    private double latitude;
    @Schema(description = "경도")
    private double longitude;
    @Schema(description = "주소")
    private String address;
    @Schema(description = "장소 설명")
    private String description;
    @Schema(description = "장소명")
    private String name;
    @Schema(description = "장소 이미지들")
    private List<String> images;
    @Schema(description = "장소 조회수")
    private int views;
    @Schema(description = "장소 리뷰 평균 평점")
    private Double ratingAvg;
    @Schema(description = "장소 리뷰 수")
    private Integer reviewCounts;

    //유저 객체 추가 필요
    @Schema(description = "장소 작성 유저")
    private String userName;

    @Schema(description = "최근 업데이트된 리뷰 목록 (최대 3개)")
    private List<RecentReviewsDto> recentReviewDtos;

    @Schema(description = "장소 생성일")
    private LocalDateTime createdAt;
    @Schema(description = "장소 수정일")
    private LocalDateTime updatedAt;

    @Schema(description = "카테고리")
    private String category;

}
