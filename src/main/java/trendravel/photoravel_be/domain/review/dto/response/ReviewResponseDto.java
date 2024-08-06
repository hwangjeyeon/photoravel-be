package trendravel.photoravel_be.domain.review.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;


@Schema(description = "리뷰 생성/수정 응답 DTO", contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ReviewResponseDto {

    @Schema(description = "리뷰 ID")
    private Long ReviewId;
    @Schema(description = "리뷰 타입")
    private String reviewType;
    @Schema(description = "리뷰 내용")
    private String content;
    @Schema(description = "리뷰 평점")
    private double rating;
    @Schema(description = "리뷰 이미지")
    private List<String> images;

    // 유저 객체 추가 필요
    @Schema(description = "리뷰 생성일")
    private LocalDateTime createdTime;
    @Schema(description = "리뷰 수정일")
    private LocalDateTime updatedTime;

}
