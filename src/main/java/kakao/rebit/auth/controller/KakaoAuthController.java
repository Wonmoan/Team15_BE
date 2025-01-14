package kakao.rebit.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.auth.service.KakaoAuthService;
import kakao.rebit.auth.jwt.AuthTokenParam;
import kakao.rebit.common.annotation.AllowAnonymous;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/auth")
@Tag(name = "인증 API", description = "카카오 로그인 관련 API")
public class KakaoAuthController {

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.url.redirect-url}")
    private String redirectUri;

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @Operation(summary = "카카오 로그인 폼", description = "카카오 로그인 버튼을 클릭하여 인가 코드를 받아오는 폼을 표시합니다.")
    @AllowAnonymous
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("redirectUri", redirectUri);
        return "loginForm";
    }

    @Operation(summary = "카카오 로그인", description = "카카오 인가 코드를 받아와 로그인 처리를 합니다.")
    @AllowAnonymous
    @GetMapping("/login/oauth/kakao")
    @ResponseBody
    public LoginResponse kakaoLogin(@RequestParam("code") String code) {
        return kakaoAuthService.kakaoLogin(code);
    }

    @Operation(summary = "카카오 로그아웃", description = "카카오 계정 로그아웃을 처리합니다.")
    @AllowAnonymous
    @GetMapping("/logout")
    public ResponseEntity<Void> kakaoLogout(@AuthTokenParam String token) {
        kakaoAuthService.kakaoLogout(token);
        return ResponseEntity.noContent().build();
    }
}
