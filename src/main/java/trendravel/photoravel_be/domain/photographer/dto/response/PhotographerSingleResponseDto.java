package trendravel.photoravel_be.domain.photographer.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;
import trendravel.photoravel_be.common.authentication.token.model.TokenResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "사진작가 생성 조회 수정 응답 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class PhotographerSingleResponseDto {
    
    
    @Schema(description = "사진작가 ID")
    Long id;
    @Schema(description = "사진작가 계정 ID")
    private String accountId;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "지역")
    private Region region;
    @Schema(description = "설명")
    private String description;
    @Schema(description = "프로필 이미지")
    private String profileImg;
    
    @Schema(description = "리뷰 별점 평균")
    private String ratingAvg;
    @Schema(description = "리뷰 수")
    private Integer reviewCount;
    
    @Schema(description = "사진작가 계정 생성일")
    private LocalDateTime createdAt;
    @Schema(description = "사진작가 계정 수정일")
    private LocalDateTime updatedAt;
    @Schema(description = "경력")
    private Integer careerYear;
    @Schema(description = "최근 리뷰")
    private List<RecentReviewsDto> recentReviewDtos;
    
    @Schema(description = "매칭 횟수")
    private Integer matchingCount;

    @Schema(description = "새로 발급된 토큰")
    private TokenResponse tokenResponse;
}
