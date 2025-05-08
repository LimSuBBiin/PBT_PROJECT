package org.execute.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.execute.domain.Mem;
import org.execute.domain.Team;
import org.execute.dto.team.*;
import org.execute.dto.team.request.MakeTeamRequest;
import org.execute.repository.MemRepository;
import org.execute.service.TeamExecuteService;
import org.execute.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization")
public class TeamController {
    private final TeamService teamService;
    private final MemRepository memRepository;
    private final TeamExecuteService teamExecuteService;

    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@RequestBody MakeTeamRequest request) {
        try {
            Team newTeam = teamService.registerTeam(request);
            System.out.println("탐 리더 ID 확인:" + newTeam.getTeamLeader().getMemMstIdx());
            return ResponseEntity.status(HttpStatus.CREATED).body(newTeam);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "팀 생성 실패: " + e.getMessage()));
        }
    }


    @GetMapping("/myteam/list")
    public ResponseEntity<List<TeamMemberQueryDto>> getMyTeamList(@RequestParam Long memMstIdx){
        try {
            List<TeamMemberQueryDto> myTeamList = teamService.getMyTeamList(memMstIdx);
            return ResponseEntity.ok(myTeamList);
        } catch (Exception e) {
            e.printStackTrace();  // 또는 로그 프레임워크 사용
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recommand/list")
    public ResponseEntity<List<TeamRequestQueryDto>> findMyTeamList() {
        try{
            List<TeamRequestQueryDto> findTeamList = teamService.findTeamList();
            return ResponseEntity.ok(findTeamList);
        } catch (Exception e) {
            e.printStackTrace();;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/recommand/list/join")
    public ResponseEntity<String> jointeam(@RequestBody TeamReqMemDto teamReqMemDto) {
        System.out.println(teamReqMemDto.getTeamIdx());
        System.out.println(teamReqMemDto.getMemMstIdx());
        try {
            teamService.requestJoinTeam(teamReqMemDto);
            return ResponseEntity.ok("가입신청 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("팀 신청 실패: " + e.getMessage());
        }
    }


    @GetMapping("/leader/myteam/members")
    public ResponseEntity<List<ExecuteMyTeamDto>> getMembers(@RequestParam Long teamIdx, @RequestParam Long memMstIdx) {

        // 로그 출력 (팀과 리더 정보 확인)
        System.out.println("내 팀 idx 확인: " + teamIdx);
        System.out.println("리더 idx 확인: " + memMstIdx);


        try {
            // 서비스 호출
            List<ExecuteMyTeamDto> myteam = teamExecuteService.getMyTeamMemberList(teamIdx, memMstIdx);


            // 팀이 정상적으로 조회된 경우
            if (myteam == null || myteam.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(myteam);  // 팀 멤버가 없다면 404 상태
            }

            // 정상적으로 조회된 경우
            return ResponseEntity.ok(myteam);  // 상태 200 OK

        } catch (Exception e) {
            // 다른 예외 발생 시
            e.printStackTrace();  // 예외의 세부사항을 로그에 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 상태 500 INTERNAL_SERVER_ERROR
        }
    }

    @GetMapping("/leader/myteam/execute")
    public ResponseEntity<List<ExecuteMyTeamDto>> executeMembers(@RequestParam Long teamIdx, @RequestParam Long memMstIdx) {
        try{

            System.out.println("내 팀 idx 확인(execute): " + teamIdx);
            System.out.println("리더 idx 확인(execute): " + memMstIdx);

            List<ExecuteMyTeamDto> executeTeam = teamExecuteService.getJoinExecuteList(teamIdx , memMstIdx);


            if(executeTeam.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(executeTeam);
            }

            return ResponseEntity.ok(executeTeam);


        } catch (Exception e) {
            // 다른 예외 발생 시
            e.printStackTrace();  // 예외의 세부사항을 로그에 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 상태 500 INTERNAL_SERVER_ERROR
        }

    }

    @GetMapping("/find")
    public ResponseEntity<Long> findTeamIdxByLeader (@RequestParam Long memMstIdx){
        System.out.println("Received request to find team idx for leader with memMstIdx: " + memMstIdx);
        Long leaderIdx = teamExecuteService.findTeamIdxByLeaderIdx(memMstIdx);
        return ResponseEntity.ok(leaderIdx);
    }

    @PutMapping("/approve")
    public ResponseEntity<String> approveMem(@RequestParam Long teamLeaderIdx, @RequestParam Long teamRequestIdx, @RequestParam Long teamIdx){
        teamExecuteService.approveMem(teamLeaderIdx,teamRequestIdx,teamIdx);
        return ResponseEntity.ok("회원 승인 완료!");
    }


    @PutMapping("/reject")
    public ResponseEntity<String> rejectMem(@RequestParam Long teamLeaderIdx, @RequestParam Long teamRequestIdx, @RequestParam Long teamIdx){
        teamExecuteService.rejectMem(teamLeaderIdx, teamRequestIdx, teamIdx);
        return ResponseEntity.ok("회원 거절 완료!");
    }



    @GetMapping("/select/myTeam")
    public ResponseEntity<List<GetTeamListDto>> getMyTeamsByMem(@RequestParam Long memMstIdx){
        System.out.println("memMstIdx: "+memMstIdx);
       List<GetTeamListDto> getTeamListDto = teamService.findMyteam(memMstIdx);
       return ResponseEntity.ok(getTeamListDto);
    }
}

