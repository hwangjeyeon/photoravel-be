package trendravel.photoravel_be.domain.guidebook.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.domain.guidebook.dto.response.GuidebookListResponseDto;
import trendravel.photoravel_be.domain.guidebook.dto.response.GuidebookResponseDto;
import trendravel.photoravel_be.domain.guidebook.service.GuidebookService;

import java.util.List;

@RestController
@RequestMapping("/public/guidebooks")
@RequiredArgsConstructor
public class PublicGuidBookController {

    private final GuidebookService guidebookService;

    @Schema(description = "가이드북 목록 READ 요청")
    @GetMapping
    public Api<List<GuidebookListResponseDto>> guidebooksList(
            @RequestParam String region) {

        return Api.READ(guidebookService.getGuidebookList(region));
    }

    @Schema(description = "가이드북 상제 정보 READ 요청")
    @GetMapping("/{guidebookId}/detail")
    public Api<GuidebookResponseDto> guidebookDetail(@PathVariable Long guidebookId) {

        return Api.READ(guidebookService.getGuidebook(guidebookId));
    }
}
