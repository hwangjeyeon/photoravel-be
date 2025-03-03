package trendravel.photoravel_be.common.security.entryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import trendravel.photoravel_be.common.exception.error.ErrorCode;
import trendravel.photoravel_be.common.exception.ApiException;
import trendravel.photoravel_be.common.response.Api;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        log.info("JwtAuthenticationEntryPoint 진입");

        ApiException apiException = new ApiException(ErrorCode.UNAUTHORIZED_ERROR);

        Api<Object> apiResponse = Api.ERROR(ErrorCode.UNAUTHORIZED_ERROR);

        String responseBody = objectMapper.writeValueAsString(apiResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);

    }
}
