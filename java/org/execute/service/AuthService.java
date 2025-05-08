package org.execute.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.execute.config.TokenProvider;
import org.execute.domain.Mem;
import org.execute.dto.LoginRequest;
import org.execute.dto.LoginResponse;
import org.execute.dto.SignUpRequest;
import org.execute.repository.MemRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemRepository memRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

//    public AuthService(MemRepository memRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
//        this.memRepository = memRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.tokenProvider = tokenProvider;
//    }

    //회원가입
    @Transactional
    public void registerUser(SignUpRequest request) {
        // 중복 체크 (MEM_ACT이 이미 존재하는지)
        System.out.println("🔍 회원 중복 체크: " + request.getMemAct());
        Optional<Mem> existingMem = memRepository.findByMemAct(request.getMemAct());
        System.out.println("🔍 회원 검색 결과: " + existingMem);
        if (existingMem.isPresent()) {
            throw new RuntimeException("이미 존재하는 회원입니다.");
        }

        System.out.println("🔍 비밀번호 암호화 전: " + request.getMemPwd());
        String encodedPassword = passwordEncoder.encode(request.getMemPwd());
        System.out.println("🔍 비밀번호 암호화 후: " + encodedPassword);
        // 비밀번호 암호화 후 저장
        Mem newUser = Mem.builder()
                .memAct(request.getMemAct())
                .memPwd(passwordEncoder.encode(request.getMemPwd()))
                .build();
        System.out.println("🔍 저장할 회원 정보: " + newUser);
        memRepository.save(newUser);
        System.out.println("✅ 회원 저장 완료");
    }

    //로그인
    @Transactional
    public LoginResponse login(LoginRequest request) {
        Optional<Mem> memOpt = memRepository.findByMemAct(request.getMemAct());

        if(memOpt.isEmpty() || !passwordEncoder.matches(request.getMemPwd(), memOpt.get().getMemPwd())){
            throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        Mem mem = memOpt.get();
        String accessToken = tokenProvider.createAccessToken(mem);
        String refreshToken = tokenProvider.createRefreshToken(mem);




        // ✅ 생성된 토큰이 null인지 확인
        System.out.println("Generated AccessToken: " + accessToken);
        System.out.println("Generated RefreshToken: " + refreshToken);
        return new LoginResponse(accessToken, refreshToken, mem.getMemMstIdx());
    }


    public Mem findByMemAct(String memAct) {
        return memRepository.findByMemAct(memAct).orElseThrow(()->new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
