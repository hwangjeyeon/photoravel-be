package trendravel.photoravel_be.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.domain.member.dto.MemberUpdateResponse;
import trendravel.photoravel_be.domain.member.dto.MemberUpdateRequest;
import trendravel.photoravel_be.domain.member.dto.MemberResponse;
import trendravel.photoravel_be.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private/member")
public class MemberPrivateController {

    private final MemberService memberService;

    @PatchMapping(value = "/info",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<MemberUpdateResponse> patchInfo(
            @RequestPart(value = "request") MemberUpdateRequest request,
            @RequestPart(value = "image") MultipartFile image
    ) {
        MemberUpdateResponse response = memberService.memberUpdate(request, image);
        return Api.OK(response);
    }

    @DeleteMapping("/{memberId}")
    public Api<?> delete(
            @PathVariable String memberId
    ) {
        memberService.delete(memberId);
        return Api.OK("deleted");
    }
}
