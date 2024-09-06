package trendravel.photoravel_be.domain.token.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import trendravel.photoravel_be.commom.error.ErrorCode;
import trendravel.photoravel_be.commom.error.MemberErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.domain.token.helper.TokenHelper;
import trendravel.photoravel_be.db.inmemorydb.entity.Token;
import trendravel.photoravel_be.domain.token.model.TokenDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final TokenHelper tokenHelper;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Token> redisTemplate;

    public TokenDto issueAccessToken(String email) {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));

        HashMap<String, Object> data = new HashMap<>();
        data.put("email", member.getEmail());
        data.put("name", member.getName());
        data.put("nickname", member.getNickname());
        data.put("memberId", member.getMemberId());
        return tokenHelper.issueAccessToken(data);
    }

    public TokenDto issueAccessTokenForMemberId(String memberId) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));

        HashMap<String, Object> data = new HashMap<>();
        data.put("email", member.getEmail());
        data.put("name", member.getName());
        data.put("nickname", member.getNickname());
        data.put("memberId", member.getMemberId());
        return tokenHelper.issueAccessToken(data);
    }

    public TokenDto issueRefreshToken(String email) {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));
        HashMap<String, Object> data = new HashMap<>();
        data.put("email", member.getEmail());
        data.put("name", member.getName());
        data.put("nickname", member.getNickname());
        data.put("memberId", member.getMemberId());

        TokenDto refreshToken = tokenHelper.issueRefreshToken(data);
        saveRefreshToken(refreshToken, member.getMemberId());

        return refreshToken;
    }

    public String validationTokenWithEmail(String token){

        Map<String, Object> data = tokenHelper.validationTokenWithThrow(token);
        Object userEmail = data.get("email");
        Objects.requireNonNull(userEmail, () -> {
            throw new ApiException(ErrorCode.NULL_POINT);
        });
        return userEmail.toString();
    }

    public String validationTokenWithMemberId(String token){

        Map<String, Object> data = tokenHelper.validationTokenWithThrow(token);
        Object memberId = data.get("memberId");
        Objects.requireNonNull(memberId, () -> {
            throw new ApiException(ErrorCode.NULL_POINT);
        });
        return memberId.toString();
    }

    public Token saveRefreshToken(TokenDto tokendto, String memberId) {
        Token token = Token.builder()
                .id(memberId)
                .refreshToken(tokendto.getToken())
                .build();

        redisTemplate.opsForHash().put("refresh_token", token.getId(), token);
        return token;
    }

}
