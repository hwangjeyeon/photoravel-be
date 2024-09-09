package trendravel.photoravel_be.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.domain.member.dto.MemberModRequest;
import trendravel.photoravel_be.domain.member.dto.MemberResponse;
import trendravel.photoravel_be.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private/member")
public class MemberPrivateController {

    private final MemberService memberService;

    @GetMapping("/info/{memberId}")
    public Api<MemberResponse> getMemberInfo(
            @PathVariable String memberId
    ) {
        MemberResponse response = memberService.getMemberInfo(memberId);
        return Api.OK(response);
    }

    @PatchMapping("/info")
    public Api<MemberResponse> patchInfo(MemberModRequest request) {
        MemberResponse response = memberService.memberModi(request);
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
