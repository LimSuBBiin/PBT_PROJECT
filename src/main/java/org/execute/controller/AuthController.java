package org.execute.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.execute.config.JwtAuthenticationFilter;
import org.execute.config.TokenProvider;
import org.execute.domain.Mem;
import org.execute.dto.LoginRequest;
import org.execute.dto.LoginResponse;
import org.execute.dto.SignUpRequest;
import org.execute.repository.MemRepository;
import org.execute.service.AuthService;
import org.execute.service.MyPageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization", allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemRepository memRepository;
    private final MyPageService myPageService;
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
        try {
            authService.registerUser(request);
            return ResponseEntity.ok("회원가입 완료");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입 실패: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response){
        Mem mem = authService.findByMemAct(request.getMemAct());
        System.out.println("PK값 출력"+ mem.getMemMstIdx());
        LoginResponse loginresponse = authService.login(request);

        // ✅ 응답 데이터 로그 확인
        System.out.println("Response Data: " + loginresponse.getAccessToken());

        Cookie accessTokenCookie = new Cookie("accessToken", loginresponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true); // JS에서 접근 불가
        accessTokenCookie.setSecure(true); // HTTPS 환경에서만 쿠키 전송
        accessTokenCookie.setPath("/"); // 모든 경로에서 쿠키 사용 가능
        accessTokenCookie.setMaxAge(60 * 30); // 30분 만료

        // ✅ Refresh Token 쿠키 생성
        Cookie refreshTokenCookie = new Cookie("refreshToken", loginresponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일 만료


        // ✅ 쿠키 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 로그인 성공 후 마이페이지로 리디렉션
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Location", "/profile/mypage?memMstIdx=" + mem.getMemMstIdx());  // 마이페이지 URL로 리디렉션
        return ResponseEntity.ok(loginresponse);
    }

    //토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletResponse response, HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")){
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken이 존재하지 않습니다.");
        }

        if (tokenProvider.validateToken(refreshToken)== null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token이 유효하지 않습니다.");
        }

        String username = jwtAuthenticationFilter.getUsernameFromToken(refreshToken);
        Optional<Mem> memOptional = memRepository.findByMemAct(username);

        if(memOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다.");
        }
        long accessTokenExpiry = 30 * 60 * 1000;
        Mem user = memOptional.get();
        // 5️⃣ 새 Access Token 생성
        String newAccessToken = tokenProvider.createToken(user,accessTokenExpiry);

        // 6️⃣ 새로운 Access Token을 쿠키에 저장
        Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
        newAccessTokenCookie.setHttpOnly(true);
        newAccessTokenCookie.setSecure(true);
        newAccessTokenCookie.setPath("/");
        newAccessTokenCookie.setMaxAge(60 * 30); // 30분 만료

        response.addCookie(newAccessTokenCookie);

        // 7️⃣ 응답 반환 (새 Access Token 포함)
        return ResponseEntity.ok().body("새로운 Access Token이 발급되었습니다.");
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        // ✅ 만료된 쿠키 설정 (삭제)
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok("로그아웃 완료");
    }


    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request){
        String token = jwtAuthenticationFilter.resolveToken(request);
        if(token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 존재하지 않습니다.");
        }

        String username = jwtAuthenticationFilter.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        }

        // 사용자 정보를 기반으로 응답 데이터 준비
        Optional<Mem> mem = memRepository.findByMemAct(username);
        if (mem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(mem.get());
    }



}
