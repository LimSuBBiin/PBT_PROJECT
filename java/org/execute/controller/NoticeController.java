package org.execute.controller;

import lombok.RequiredArgsConstructor;
import org.execute.domain.GameRequest;
import org.execute.domain.NoticeGame;
import org.execute.domain.Position;
import org.execute.dto.GameRequest.GameRequestDto;
import org.execute.dto.notice.*;
import org.execute.service.GameRequestService;
import org.execute.service.NoticeDetailService;
import org.execute.service.NoticeGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization", allowCredentials = "true")
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeGameService noticeGameService;
    private final NoticeDetailService noticeDetailService;
    private final GameRequestService gameRequestService;
    
    

    @PostMapping("/create")
    public ResponseEntity<?> createNotice(@RequestBody CreateNoticeDto createNoticeDto){
        try{
            NoticeGame noticeGame = noticeGameService.createNotice(createNoticeDto);
            System.out.println("글 생성합니다. noticeidx값 확인: " + noticeGame.getNoticeGameIdx());
            return ResponseEntity.status(HttpStatus.CREATED).body(noticeGame);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "게시판 생성 실패" + e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<NoticeListDto>>  getNoticeList(@RequestParam Long teamIdx, @RequestParam Long teamRequestIdx) {
        try{
            System.out.println("Controller에서 notice_List의 각각의 team, tm_ id값 확인: "+teamIdx + " " + teamRequestIdx);
            List<NoticeListDto> findNoticeList = noticeGameService.getNoticeList(teamIdx, teamRequestIdx);
            System.out.println(("해당 팀의 글 조회합니다. teamIdx값과 teamRequestIdx값 확인: " + teamIdx + teamRequestIdx));
            if(findNoticeList == null) {
                System.out.println("findNoticeList == Null");
            }
            return ResponseEntity.ok(findNoticeList);
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Controller returns null");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // 내가 쓴 글 조회하기(내팀에서)
    @GetMapping("/mylist")
    public ResponseEntity<List<NoticeListDto>> getMyNoticeList(@RequestParam Long teamIdx, @RequestParam Long teamRequestIdx, @RequestParam Long memMstIdx){
        try {
            System.out.println("Controller에서 notice_List의 각각의 team, tm_ id, memMstIdx값 확인:"+teamIdx+" "+teamRequestIdx+ " " + memMstIdx);
            List<NoticeListDto> findmyNoticeList = noticeGameService.getMyNoticeList(teamIdx,teamRequestIdx,memMstIdx);
            if(findmyNoticeList == null) {
                System.out.println("findMyNoticeList == NULL ");
            }
            return ResponseEntity.ok(findmyNoticeList);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Controller returns null");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/myReq")
    public ResponseEntity<List<NoticeRequestListDto>> getMyNoticeList(@RequestParam Long memMstIdx) {
        try{
            List<NoticeRequestListDto> findmyReq = noticeGameService.geyMyGameReqNotice(memMstIdx);
            if(findmyReq == null) {
                System.out.println("Controller에서 null");
            }
            return ResponseEntity.ok(findmyReq);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Controller returns null");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getTeamRequestIdx")
    public ResponseEntity<Optional<Long>> getTeamRequestIdx(@RequestParam Long teamIdx, @RequestParam Long memMstIdx){
        System.out.println("Controller에서의 Id값 확인:"+teamIdx + memMstIdx);
        try {
            Optional<Long> userTeamRequestIdx = noticeGameService.findIdxByMemAndTeam(teamIdx, memMstIdx);
            return ResponseEntity.ok(userTeamRequestIdx);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        }



    @GetMapping("/detail")
    public ResponseEntity<NoticeDetailDto> getNoticeDetail(@RequestParam Long noticeGameIdx){
        try {
            System.out.println("Controller에서 noticeGameIdx값 확인: " + noticeGameIdx);

            NoticeDetailDto noticeDetailDto = noticeDetailService.getNoticeDetail(noticeGameIdx);
            noticeGameService.hitImpl(noticeGameIdx);
            return ResponseEntity.ok(noticeDetailDto);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Controller에서 문제 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/detail/request")
    public ResponseEntity<GameReqFoamDto> getReqFoam(@RequestParam Long memMstIdx, @RequestParam Long noticeGameIdx){
        try{
            System.out.println("Controller에서 memMstIdx값 확인: "+memMstIdx);
            System.out.println("Controller에서 noticeGameIdx값 확인:"+noticeGameIdx);
            GameReqFoamDto gameReqFoamDto = noticeDetailService.getReqFoam(memMstIdx);
            return ResponseEntity.ok(gameReqFoamDto);
        } catch (Exception e) {
            e.printStackTrace();;
            System.out.println("Controller에서 문제 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/detail/request")
    public ResponseEntity<?> joinGame(@RequestBody GameRequestDto gameRequestDto){

        try{
            System.out.println("Controller에서 teamMember idx값확인: "+gameRequestDto.getTeamRequestIdx());
            System.out.println("Controller에서 notice idx값 확인: "+gameRequestDto.getNoticeGameIdx());
            gameRequestService.joinRequest(gameRequestDto);
            return ResponseEntity.ok(Map.of("message", "해당 게임 신청완료"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "팀 신청 실패: " + e.getMessage()));
        }
    }

 //request에 대한
    @PutMapping("/request/cancel")
    public ResponseEntity<String> cancelReq(@RequestParam Long noticeGameIdx, @RequestParam Long teamRequestIdx){
        gameRequestService.cacelReq(noticeGameIdx,teamRequestIdx);
        return ResponseEntity.ok("해당 경기 취소완료");
    }

    @PutMapping("/request/rejoin")
    public ResponseEntity<String> reJoinReq(@RequestParam Long noticeGameIdx, @RequestParam Long teamRequestIdx, @RequestParam Position position) {
        gameRequestService.reJoinReq(noticeGameIdx,teamRequestIdx, position);
        return ResponseEntity.ok("해당 경기 재신청 완료!");
    }



//    @GetMapping("/getNoticeList")
//    public ResponseEntity<List<NoticeListDto>> getNoticeList(@RequestParam Long teamIdx, @RequestParam Long teamRequestIdx){
//        System.out.println("Controller에서 notice_List의 각각의 team, tm_ id값 확인: "+teamIdx + " " + teamRequestIdx);
//        try
//    }
//
}
