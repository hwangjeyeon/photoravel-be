package trendravel.photoravel_be.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.common.exception.error.MemberErrorCode;
import trendravel.photoravel_be.common.exception.ApiException;
import trendravel.photoravel_be.common.image.service.ImageServiceFacade;
import trendravel.photoravel_be.db.inmemorydb.entity.Token;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.domain.authentication.service.MemberAuthenticationService;
import trendravel.photoravel_be.domain.authentication.session.UserSession;
import trendravel.photoravel_be.domain.member.convertor.MemberConvertor;
import trendravel.photoravel_be.domain.member.dto.*;
import trendravel.photoravel_be.domain.token.model.TokenDto;
import trendravel.photoravel_be.domain.token.model.TokenResponse;
import trendravel.photoravel_be.domain.token.service.TokenService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberConvertor memberConvertor;
    private final TokenService tokenService;
    private final PasswordEncoder memberBCryptPasswordEncoder;
    private final RedisTemplate<String, Token> redisTemplate;
    private final MemberAuthenticationService authenticationService;
    private final ImageServiceFacade imageServiceFacade;

    @Transactional
    public TokenResponse login(BaseMemberDto baseMemberDto) {
        MemberEntity member = memberRepository.findByEmail(baseMemberDto.getEmail())
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));
        return issueTokenResponse(member);
    }


    @Transactional
    public TokenResponse addInfoWithLogin(CompleteMemberDto memberDto) {

        MemberEntity memberEntity = MemberEntity.builder()
                .memberId(memberDto.getProvider() + "_" + memberDto.getId())
                .password(memberBCryptPasswordEncoder.encode(memberDto.getProvider() + "_" + memberDto.getId() + "key"))
                .email(memberDto.getEmail())
                .name(memberDto.getName())
                .nickname(memberDto.getNickname())
                .profileImg(memberDto.getProfileImg())
                .build();

        MemberEntity saved = memberRepository.save(memberEntity);
        log.info("saved member : {}", saved.getEmail());

        return issueTokenResponse(saved);
    }

    @Transactional
    public MemberResponse localRegister(MemberRegisterRequest request, MultipartFile image) {
        String profileImgUrl;
        if (image == null || image.isEmpty()) {
            profileImgUrl = "https://photravle-images.shop/default_profile.png";
        } else {
            profileImgUrl = imageServiceFacade.uploadImageFacade(List.of(image)).get(0);
        }
        MemberEntity memberEntity = MemberEntity.builder()
                .memberId(request.getMemberId())
                .password(memberBCryptPasswordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .profileImg(profileImgUrl)
                .build();
        MemberEntity saved = memberRepository.save(memberEntity);

        return memberConvertor.toMemberResponse(saved);
    }

    @Transactional
    public MemberResponse getMemberInfo(String memberId) {

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));

        return memberConvertor.toMemberResponse(memberEntity);
    }

    @Transactional
    public MemberUpdateResponse memberUpdate(MemberUpdateRequest request, MultipartFile image) {

        UserSession principal = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MemberEntity memberEntity = memberRepository.findByMemberId(principal.getUsername())
                .orElseThrow(() -> new ApiException(MemberErrorCode.UNAUTHORIZED));

        redisTemplate.opsForHash().delete("member_refresh_token", memberEntity.getMemberId());
        // TODO..............(?)
        // 인증 객체랑 토큰 안의 회원 정보랑 비교 검사를 해야할까..?
        // 왜냐하면 다른 회원이 정보를 수정할지도 모르니까

        if (image != null) {
            memberEntity.updateMember(
                    request.getMemberId(),
                    request.getPassword(),
                    request.getName(),
                    request.getNickname(),
                    request.getEmail(),
                    imageServiceFacade.updateImageFacade(List.of(image), List.of(memberEntity.getProfileImg())).toString()
            );
        } else {
            memberEntity.updateMember(
                    request.getMemberId(),
                    request.getPassword(),
                    request.getName(),
                    request.getNickname(),
                    request.getEmail()
            );
        }
        TokenResponse tokenResponse = issueTokenResponse(memberEntity);

        // 회원 정보 수정 후 UserDetails를 업데이트
        UserSession userSession = (UserSession) authenticationService.loadUserByUsername(memberEntity.getMemberId());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userSession, null, userSession.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return MemberUpdateResponse.builder()
                .memberId(memberEntity.getMemberId())
                .email(memberEntity.getEmail())
                .password(memberEntity.getPassword())
                .name(memberEntity.getName())
                .nickname(memberEntity.getNickname())
                .profileImg(memberEntity.getProfileImg())
                .createdAt(memberEntity.getCreatedAt())
                .updatedAt(memberEntity.getUpdatedAt())
                .token(tokenResponse)
                .build();
    }

    @Transactional
    public MemberInfoCheckResponse emailCheck(String email) {

        boolean result = memberRepository.findByEmail(email).isPresent();

        return MemberInfoCheckResponse.builder()
                .isDuplicated(result)
                .build();
    }

    @Transactional
    public MemberInfoCheckResponse nicknameCheck(String nickname) {
        boolean result = memberRepository.findByNickname(nickname).isPresent();

        return MemberInfoCheckResponse.builder()
                .isDuplicated(result)
                .build();
    }

    @Transactional
    public MemberInfoCheckResponse memberIdCheck(String memberId) {
        boolean result = memberRepository.findByMemberId(memberId).isPresent();

        return MemberInfoCheckResponse.builder()
                .isDuplicated(result)
                .build();
    }

    @Transactional
    public void delete(String memberId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSession principal = (UserSession) authentication.getPrincipal();

        MemberEntity member = memberRepository.findByMemberId(principal.getUsername())
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));
        log.info("find member : {}, memberId : {}", member.getMemberId(), memberId);

        if (member.getMemberId().equals(memberId)){
            String img = member.getProfileImg();
            memberRepository.delete(member);
            imageServiceFacade.deleteAllImagesFacade(List.of(img));
            SecurityContextHolder.clearContext();
            redisTemplate.opsForHash().delete("member_refresh_token", memberId);
        }else {
            throw new ApiException(MemberErrorCode.UNAUTHORIZED);
        }
    }

    @Transactional
    public TokenResponse localLogin(MemberLoginRequest request) {
        MemberEntity memberEntity = memberRepository.findByMemberId(request.getMemberId())
                .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_ID_NOT_MATCH));
        if (memberBCryptPasswordEncoder.matches(request.getPassword(), memberEntity.getPassword())) {
            return issueTokenResponse(memberEntity);
        } else {
            throw new ApiException(MemberErrorCode.PASSWORD_NOT_MATCH);
        }
    }


    private TokenResponse issueTokenResponse(MemberEntity member) {
        TokenDto accessTokenDto = tokenService.issueAccessToken(member.getMemberId());
        TokenDto refreshTokenDto = tokenService.issueRefreshToken(member.getMemberId());

        return TokenResponse.builder()
                .accessToken(accessTokenDto)
                .refreshToken(refreshTokenDto)
                .build();
    }
}
