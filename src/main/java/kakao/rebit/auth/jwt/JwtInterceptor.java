package kakao.rebit.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kakao.rebit.common.annotation.AllowAnonymous;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // CORS preflight 요청은 토큰 검증을 하지 않음
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        // AllowAnonymous 어노테이션이 붙어있는 경우 토큰 검증을 하지 않음
        if (handler instanceof HandlerMethod handlerMethod && handlerMethod.getMethodAnnotation(AllowAnonymous.class) != null) {
            return true;
        }

        String token = jwtTokenProvider.extractToken(request.getHeader(AUTHORIZATION_HEADER));

        return jwtTokenProvider.validateToken(token);
    }
}
