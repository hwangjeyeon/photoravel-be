package trendravel.photoravel_be.domain.location.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class LocationMultiReadResponseDto {

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

    private int views;

    private String ratingAvg;

    //유저 객체 추가 필요

    // 프론트엔드측과 의논 후 추가 여부 결정
    // private List<RecentReviewsDto> recentReviewDtos;

    @Schema(description = "장소 생성일")
    private LocalDateTime createdTime;
    @Schema(description = "장소 수정일")
    private LocalDateTime updatedTime;

}
