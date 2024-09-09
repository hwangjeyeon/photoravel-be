package trendravel.photoravel_be.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.domain.member.dto.MemberInfoCheckResponse;
import trendravel.photoravel_be.domain.member.dto.MemberLoginRequest;
import trendravel.photoravel_be.domain.member.dto.MemberRegisterRequest;
import trendravel.photoravel_be.domain.member.dto.MemberResponse;
import trendravel.photoravel_be.domain.member.service.MemberService;
import trendravel.photoravel_be.domain.token.model.TokenResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/member")
public class MemberPublicController {

    private final MemberService memberService;

    @PostMapping("/register")
    public Api<MemberResponse> localRegister(
            @RequestBody MemberRegisterRequest request
    ) {
        MemberResponse response = memberService.localRegister(request);

        return Api.CREATED(response);
    }

    @PostMapping("/local")
    public Api<TokenResponse> localLogin(
            @RequestBody MemberLoginRequest request
    ) {
        TokenResponse response = memberService.localLogin(request);
        return Api.OK(response);
    }


    @GetMapping("/{email}/email-check")
    public Api<MemberInfoCheckResponse> emailCheck(
            @PathVariable String email
    ) {
        MemberInfoCheckResponse response = memberService.emailCheck(email);
        return Api.OK(response);
    }

    @GetMapping("/{nickname}/nickname-check")
    public Api<MemberInfoCheckResponse> nicknameCheck(
            @PathVariable String nickname
    ) {
        MemberInfoCheckResponse response = memberService.nicknameCheck(nickname);
        return Api.OK(response);
    }

    @GetMapping("/{memberId}/memberId-check")
    public Api<MemberInfoCheckResponse> memberIdCheck(
            @PathVariable String memberId
    ) {
        MemberInfoCheckResponse response = memberService.memberIdCheck(memberId);
        return Api.OK(response);
    }
}
