package org.execute.controller;

import lombok.RequiredArgsConstructor;
import org.execute.domain.Mem;
import org.execute.domain.Team;
import org.execute.dto.team.TeamMemberQueryDto;
import org.execute.dto.team.TeamReqMemDto;
import org.execute.dto.team.TeamRequestQueryDto;
import org.execute.dto.team.request.MakeTeamRequest;
import org.execute.repository.MemRepository;
import org.execute.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization")
public class TeamController {
    private final TeamService teamService;
    private final MemRepository memRepository;

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
}

