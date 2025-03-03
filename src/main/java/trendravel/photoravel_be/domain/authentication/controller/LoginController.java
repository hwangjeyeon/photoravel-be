package trendravel.photoravel_be.domain.authentication.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trendravel.photoravel_be.common.response.Api;
import trendravel.photoravel_be.domain.authentication.oauth2.service.SocialLoginService;
import trendravel.photoravel_be.domain.member.dto.BaseMemberDto;
import trendravel.photoravel_be.domain.member.dto.CompleteMemberDto;
import trendravel.photoravel_be.domain.member.service.MemberService;
import trendravel.photoravel_be.domain.token.model.TokenResponse;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final SocialLoginService socialLoginService;
    private final MemberService memberService;

    @GetMapping("/oauth2/{provider}")
    public void tryOauth2(@PathVariable String provider, HttpServletResponse response) throws IOException {
        String url = socialLoginService.tryOauth2(provider);
        log.info("created url = {}", url);
        response.sendRedirect(url);
    }

    @GetMapping("/oauth2/code/{provider}")
    public ResponseEntity<?> authorized(@PathVariable String provider, @RequestParam("code") String code) {
        log.info("authorized - provider : {}, code : {}", provider, code);
        return socialLoginService.connectToSocialLogin(provider, code);
    }

    @PostMapping("/social/{provider}")
    public Api<?> socialSignIn(@PathVariable String provider, @RequestBody String code ){

        log.info("socialSignIn - provider : {}, code : {}", provider, code);
        BaseMemberDto baseMemberDto = socialLoginService.login(provider, code);
        log.info("signUpForm = {}", baseMemberDto.getEmail());

        // 이미 소셜 가입되어 있는 경우
        if (baseMemberDto.isExist()) {
            TokenResponse response = memberService.login(baseMemberDto);
            return Api.OK(response);
        // 최초 소셜 가입인 경우 dto 반환 후 멤버에 대한 추가 입력을 받은 후 addInfo 호출
        } else {
            return Api.CREATED(baseMemberDto);
        }
    }

    @PostMapping("/addInfo")
    public Api<TokenResponse> addInfo(@RequestBody CompleteMemberDto memberDto ) {
        log.info("memberDto : {}", memberDto.getEmail());

        TokenResponse response = memberService.addInfoWithLogin(memberDto);

        return Api.OK(response);
    }


}
