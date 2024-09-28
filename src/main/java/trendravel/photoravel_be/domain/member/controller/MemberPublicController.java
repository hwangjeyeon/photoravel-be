package trendravel.photoravel_be.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping(value = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<MemberResponse> localRegister(
            @RequestPart(value = "request") MemberRegisterRequest request,
            @RequestPart(value = "image") MultipartFile image
            ) {
        // 생각해보니 이미지를 필수로 넣어야하나?
        // 이미지를 첨부하지 않으면 default img가 필요할 것 같은데..
        MemberResponse response = memberService.localRegister(request, image);

        return Api.CREATED(response);
    }

    @PostMapping("/local")
    public Api<TokenResponse> localLogin(
            @RequestBody MemberLoginRequest request
    ) {
        TokenResponse response = memberService.localLogin(request);
        return Api.OK(response);
    }

    @GetMapping("/info/{memberId}")
    public Api<MemberResponse> getMemberInfo(
            @PathVariable String memberId
    ) {
        MemberResponse response = memberService.getMemberInfo(memberId);
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
