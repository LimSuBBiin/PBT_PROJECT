package org.execute.controller;

import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemRepository memRepository;
    private final MyPageService myPageService;
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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        Mem mem = authService.findByMemAct(request.getMemAct());
        System.out.println("PK값 출력"+ mem.getMemMstIdx());
        LoginResponse response = authService.login(request);

        // ✅ 응답 데이터 로그 확인
        System.out.println("Response Data: " + response.getAccessToken());

        // 로그인 성공 후 마이페이지로 리디렉션
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/profile/mypage?memMstIdx=" + mem.getMemMstIdx());  // 마이페이지 URL로 리디렉션
        return ResponseEntity.ok(response);
    }


    @GetMapping("/h1")
    public List<Mem> mem1() {
        List<Mem> all = memRepository.findByAllMem();
        return all;
    }

}
