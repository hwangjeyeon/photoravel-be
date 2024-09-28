package trendravel.photoravel_be.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import trendravel.photoravel_be.commom.accessHandler.JwtAccessDeniedHandler;
import trendravel.photoravel_be.commom.entryPoint.JwtAuthenticationEntryPoint;
import trendravel.photoravel_be.db.inmemorydb.entity.Token;
import trendravel.photoravel_be.domain.authentication.service.MemberAuthenticationService;
import trendravel.photoravel_be.domain.authentication.service.PhotographerAuthenticationService;
import trendravel.photoravel_be.domain.token.service.TokenService;
import trendravel.photoravel_be.filter.JwtMemberAuthenticationFilter;
import trendravel.photoravel_be.filter.JwtExceptionFilter;
import trendravel.photoravel_be.filter.JwtPhotographerAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final MemberAuthenticationService authenticationService;
    private final PhotographerAuthenticationService photographerAuthenticationService;
    private final TokenService tokenService;
    private final RedisTemplate<String, Token> redisTemplate;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        return http
                .httpBasic(AbstractHttpConfigurer::disable) //rest api를 사용하므로 비활성화
                .csrf(AbstractHttpConfigurer::disable)  // token을 사용하므로 비활성화
                .cors(custom -> custom.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L); //1시간
                        return config;
                    }
                }))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> {
                    configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/private/photographers/**").hasRole("PHOTOGRAPHER");
                    request.requestMatchers("/private/member/**").hasRole("MEMBER");
                    request.requestMatchers("/private/**").authenticated();
                    request.requestMatchers("/swagger-ui.html").permitAll();
                    request.requestMatchers("/swagger-ui/**").permitAll();
                    request.requestMatchers("/v3/api-docs/**").permitAll();
                    request.requestMatchers("/public/**").permitAll();
                    request.requestMatchers("/login/**").permitAll();
                    request.anyRequest().permitAll();
                })
                .addFilterBefore(new JwtExceptionFilter(objectMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtMemberAuthenticationFilter(authenticationService, tokenService, redisTemplate), JwtExceptionFilter.class)
                .addFilterAfter(new JwtPhotographerAuthenticationFilter(photographerAuthenticationService, tokenService, redisTemplate), JwtMemberAuthenticationFilter.class)
                .exceptionHandling(configurer -> {
                    configurer.accessDeniedHandler(new JwtAccessDeniedHandler(objectMapper));
                    configurer.authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper));
                })
                .build();
    }

    @Bean
    public BCryptPasswordEncoder memberBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
