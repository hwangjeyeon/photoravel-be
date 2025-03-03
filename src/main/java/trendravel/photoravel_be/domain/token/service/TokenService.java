package trendravel.photoravel_be.domain.token.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import trendravel.photoravel_be.common.exception.error.ErrorCode;
import trendravel.photoravel_be.common.exception.error.MemberErrorCode;
import trendravel.photoravel_be.common.exception.error.PhotographerErrorCode;
import trendravel.photoravel_be.common.exception.ApiException;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.db.respository.photographer.PhotographerRepository;
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
    private final PhotographerRepository photographerRepository;
    private final RedisTemplate<String, Token> redisTemplate;

    public TokenDto issueAccessToken(String memberId) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));

        HashMap<String, Object> data = new HashMap<>();
        data.put("email", member.getEmail());
        data.put("name", member.getName());
        data.put("nickname", member.getNickname());
        data.put("memberId", member.getMemberId());
        data.put("role", "member");
        data.put("issuer", "photoravel");
        return tokenHelper.issueAccessToken(data);
    }

    public TokenDto issueRefreshToken(String memberId) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));
        HashMap<String, Object> data = new HashMap<>();
        data.put("email", member.getEmail());
        data.put("name", member.getName());
        data.put("nickname", member.getNickname());
        data.put("memberId", member.getMemberId());
        data.put("role", "member");
        data.put("issuer", "photoravel");
        TokenDto refreshToken = tokenHelper.issueRefreshToken(  data);
        saveRefreshToken(refreshToken, member.getMemberId());

        return refreshToken;
    }

    public TokenDto issuePhotographerAccessToken(String accountId) {
        Photographer photographer = photographerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));

        HashMap<String, Object> data = new HashMap<>();
        data.put("accountId", photographer.getAccountId());
        data.put("name", photographer.getName());
        data.put("role", "photographer");
        data.put("issuer", "photoravel");
        return tokenHelper.issueAccessToken(data);
    }

    public TokenDto issuePhotographerRefreshToken(String accountId) {
        Photographer photographer = photographerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));

        HashMap<String, Object> data = new HashMap<>();
        data.put("accountId", photographer.getAccountId());
        data.put("name", photographer.getName());
        data.put("role", "photographer");
        data.put("issuer", "photoravel");
        TokenDto refreshToken = tokenHelper.issueRefreshToken(data);
        savePhotographerRefreshToken(refreshToken, photographer.getAccountId());

        return refreshToken;
    }

    public Map<String, Object> validationTokenWithMap(String token){

        return tokenHelper.validationTokenWithThrow(token);
    }

    public String validationTokenWithMemberId(String token){

        Map<String, Object> data = tokenHelper.validationTokenWithThrow(token);
        Object memberId = data.get("memberId");
        Objects.requireNonNull(memberId, () -> {
            throw new ApiException(ErrorCode.NULL_POINT);
        });
        return memberId.toString();
    }

    public String validationTokenWithAccountId(String token){

        Map<String, Object> data = tokenHelper.validationTokenWithThrow(token);
        Object accountId = data.get("accountId");
        Objects.requireNonNull(accountId, () -> {
            throw new ApiException(ErrorCode.NULL_POINT);
        });
        return accountId.toString();
    }


    public Token saveRefreshToken(TokenDto tokendto, String memberId) {
        Token token = Token.builder()
                .id(memberId)
                .refreshToken(tokendto.getToken())
                .build();

        redisTemplate.opsForHash().put("member_refresh_token", token.getId(), token);
        return token;
    }

    public Token savePhotographerRefreshToken(TokenDto tokendto, String accountId) {
        Token token = Token.builder()
                .id(accountId)
                .refreshToken(tokendto.getToken())
                .build();

        redisTemplate.opsForHash().put("photographer_refresh_token", token.getId(), token);
        return token;
    }

}
