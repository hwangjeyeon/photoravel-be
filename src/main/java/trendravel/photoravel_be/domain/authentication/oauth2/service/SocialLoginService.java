package trendravel.photoravel_be.domain.authentication.oauth2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.domain.authentication.oauth2.Oauth2Attributes;
import trendravel.photoravel_be.domain.authentication.oauth2.social.GoogleToken;
import trendravel.photoravel_be.domain.authentication.oauth2.social.KakaoToken;
import trendravel.photoravel_be.domain.member.dto.BaseMemberDto;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocialLoginService {
    private final InMemoryClientRegistrationRepository clientRegistrationRepository;
    private final MemberRepository memberRepository;

    public String tryOauth2(String provider) {

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(provider);
        return clientRegistration.getProviderDetails().getAuthorizationUri()
                + "?client_id=" + clientRegistration.getClientId()
                + "&response_type=code"
                + "&redirect_uri=" + clientRegistration.getRedirectUri()
                + "&scope=" + String.join("+", clientRegistration.getScopes());
    }


    public ResponseEntity<?> connectToSocialLogin(String provider, String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> request = new HttpEntity<>(code, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<?> response = restTemplate.postForEntity(
                "http://localhost:8080/login/social/" + provider,
                request, Object.class
        );

        return response;
    }


    public BaseMemberDto login(String providerName, String code){
        ClientRegistration provider = clientRegistrationRepository.findByRegistrationId(providerName);

        if (providerName.equals("kakao")) {
            KakaoToken kakaoTokens = getKakaoTokens(provider, code);
            return getFormFromUserProfile(provider, kakaoTokens.getAccess_token());
        } else if (providerName.equals("google")) {
            GoogleToken googleTokens = getGoogleTokens(provider, code);
            return getFormFromUserProfile(provider, googleTokens.getAccess_token());
        } else {
            throw new RuntimeException("지원하지 않는 provider");
        }
    }

    private BaseMemberDto getFormFromUserProfile(ClientRegistration provider, String token) {
        Map<String, Object> map = (Map<String, Object>) getUserAttributes(provider, token);
        Oauth2Attributes attributes = Oauth2Attributes.of(provider.getRegistrationId(), map);
        boolean exists = memberRepository.existsByEmail(attributes.getEmail());

        log.info("member exists : {}", exists);

        return new BaseMemberDto(provider.getClientName(), attributes.getID(), attributes.getProfileImg() ,attributes.getEmail(), attributes.getName(), exists);
    }

    private Map<?, ?> getUserAttributes(ClientRegistration provider, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(provider.getProviderDetails().getUserInfoEndpoint().getUri(),
                HttpMethod.GET, request, String.class);
        try {
            return new ObjectMapper().readValue(response.getBody(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private GoogleToken getGoogleTokens(ClientRegistration provider, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(tokenRequest(provider, code), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(provider.getProviderDetails().getTokenUri(),
                request, String.class);
        try {
            return new ObjectMapper().readValue(response.getBody(), GoogleToken.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private KakaoToken getKakaoTokens(ClientRegistration provider, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(tokenRequest(provider, code), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(provider.getProviderDetails().getTokenUri(),
                request, String.class);
        try {
            return new ObjectMapper().readValue(response.getBody(), KakaoToken.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MultiValueMap<String, String> tokenRequest(ClientRegistration provider, String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("grant_type", provider.getAuthorizationGrantType().getValue());
        map.add("redirect_uri", provider.getRedirectUri());
        map.add("client_id", provider.getClientId());
        map.add("client_secret", provider.getClientSecret());
        return map;
    }
}
