package trendravel.photoravel_be.domain.spot.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "여러 스팟에 대한 READ 응답 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
public class SpotMultiReadResponseDto {

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
    @Schema(description = "각 스팟별 조회수")
    private int views;

    //유저 객체 추가 필요


    // 프론트엔드측과 의논 후 추가 여부 결정
    //private List<RecentReviewsDto> recentReviewDtos;


    // 유저 객체 전달 필요
    @Schema(description = "스팟 생성일")
    private LocalDateTime createAt;
    @Schema(description = "스팟 수정일")
    private LocalDateTime updatedAt;

}
