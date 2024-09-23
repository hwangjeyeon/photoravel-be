package trendravel.photoravel_be.domain.location.dto.response;


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
@Schema(description = "주변 모든 장소 READ 응답 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
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
    @Schema(description = "장소별 조회수")
    private int views;
    @Schema(description = "장소 리뷰 평균 평점")
    private Double ratingAvg;
    @Schema(description = "장소 리뷰 수")
    private Integer reviewCounts;


    @Schema(description = "장소 생성일")
    private LocalDateTime createAt;
    @Schema(description = "장소 수정일")
    private LocalDateTime updatedAt;

}
