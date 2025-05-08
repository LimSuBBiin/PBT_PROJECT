package org.execute.controller;

import lombok.RequiredArgsConstructor;
import org.execute.domain.Mem;
import org.execute.dto.MemDto;
import org.execute.dto.UpdateRequest;
import org.execute.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization", "Content-Type"})
public class MyPageController {

    private final MyPageService myPageService;

//    public MyPageController(MyPageService myPageService) {
//        this.myPageService = myPageService;
//    }

    @PutMapping("/edit-my-page")
    public ResponseEntity<String> updateMem(@RequestBody UpdateRequest updateRequest) {

        //유효성 검사
        if (updateRequest == null || Objects.isNull(updateRequest.getMemMstIdx())){
            return new ResponseEntity<>("잘못된 요청입니다. 사용자 정보가 없습니다.", HttpStatus.BAD_REQUEST);
        }
        try {
            myPageService.updateMem(updateRequest.getMemMstIdx(), updateRequest.getMemNme(),
                    updateRequest.getPhonHed(), updateRequest.getPhonBod(), updateRequest.getPhonTal(), updateRequest.getEmalTal(), updateRequest.getEmalHed(), updateRequest.getMemGen(), updateRequest.getMainPst(), updateRequest.getSubPst(), updateRequest.getMemWeight(), updateRequest.getMemHeight(), updateRequest.getMemHist());
        return new ResponseEntity<>("사용자 정보 수정 완료", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("서버 오류! 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }




    @GetMapping("/edit-my-page")
    public ResponseEntity<MemDto> myProfileEdit(@RequestParam Long memMstIdx){
        System.out.println("12345678");
        MemDto mem = myPageService.findMemProfile(memMstIdx);
        Long mem1 = mem.getMemMstIdx();
        System.out.println(mem1);
        System.out.println("GET PROFILE DATA: " + mem.getMemAct());
        if (mem != null) {
            System.out.println("Profile Data: " + mem1);
            System.out.println("GET PROFILE DATA: " + mem.getMemAct());
            ResponseEntity<MemDto> ok = ResponseEntity.ok(mem);
            return ok;
        } else {
            System.out.println("사용자 정보를 찾을 수 없습니다.");
            return ResponseEntity.notFound().build();
        }
    }




    @GetMapping("/my-page2")
    public ResponseEntity<Mem> getMyPage(@RequestParam Long memSeq) {
        Mem mem= myPageService.findByMemInfoSeq(memSeq);
        if (mem != null) {
            return ResponseEntity.ok(mem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/mypage")
    public ResponseEntity<MemDto> myProfile(@RequestParam Long memMstIdx){
        System.out.println("12345678");
        MemDto mem = myPageService.findMemProfile(memMstIdx);
        Long mem1 = mem.getMemMstIdx();
        System.out.println(mem1);
        System.out.println("GET PROFILE DATA: " + mem.getMemAct());
        if (mem != null) {
            System.out.println("Profile Data: " + mem1);
            System.out.println("GET PROFILE DATA: " + mem.getMemAct());
            ResponseEntity<MemDto> ok = ResponseEntity.ok(mem);
            return ok;
        } else {
            System.out.println("사용자 정보를 찾을 수 없습니다.");
            return ResponseEntity.notFound().build();
        }
    }

    }


