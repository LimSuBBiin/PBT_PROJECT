package org.execute.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.execute.domain.*;
import org.execute.dto.team.TeamMemberQueryDto;
import org.execute.dto.team.TeamReqMemDto;
import org.execute.dto.team.TeamRequestQueryDto;
import org.execute.dto.team.request.MakeTeamRequest;
import org.execute.repository.MemRepository;
import org.execute.repository.TeamMemberQueryRepository;
import org.execute.repository.TeamMemberRepository;
import org.execute.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class TeamService {
    private final TeamRepository teamRepository;
    private final MemRepository memRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamMemberQueryRepository teamMemberQueryRepository;

    @Transactional
    public Mem findByMemMstIdx(Long memMstIdx){
        return memRepository.findByIDX(memMstIdx);
    }

    @Transactional
    public Team registerTeam(MakeTeamRequest makeTeamRequest) {
        System.out.println("팀 생성 중복 체크: " +makeTeamRequest.getTeamNme() );
        Optional<Team> existingTeam = teamRepository.findByTeamNme(makeTeamRequest.getTeamNme());
        System.out.println("팀 검색 결과: " + existingTeam);
        if(existingTeam.isPresent()) {
            throw new RuntimeException("이미 존재하는 팀 혹은 팀명 입니다.");
        }

        Mem teamLeader = memRepository.findByIDX(makeTeamRequest.getTeamLeaderIdx());
        if (teamLeader == null) {
            throw new RuntimeException("팀 리더를 찾을 수 없습니다.");
        }

        Team newTeam = Team.createTeam(teamLeader, makeTeamRequest.getTeamNme(), makeTeamRequest.getTeamDesc());
        TeamMember leaderMember = TeamMember.createTeamMember(newTeam, teamLeader, RoleType.LEADER, TeamReqStatus.승인);
//        Team newTeam = Team.builder()
//                .teamNme(makeTeamRequest.getTeamNme())
//                .teamLeader(teamLeader)
//                .teamDesc(makeTeamRequest.getTeamDesc())
//                .build();


        teamRepository.makeTeam(newTeam);
        teamMemberRepository.makeTeamMember(leaderMember);
        return newTeam;
    }

    @Transactional
    public List<TeamMemberQueryDto> getMyTeamList(Long memMstIdx){
        return teamMemberQueryRepository.getMyTeamList(memMstIdx);
    }


    //팀 추천 리스트
    @Transactional
    public List<TeamRequestQueryDto> findTeamList() {
        return teamMemberQueryRepository.findTeamList();
    }

    @Transactional
    public void requestJoinTeam(TeamReqMemDto requestDto) {

        Team team1 = teamRepository.findByTeamIdx(requestDto.getTeamIdx());
        System.out.println("team Idx값 :"+team1);
        Mem mem1 = memRepository.findByIDX(requestDto.getMemMstIdx());
        System.out.println("memIDX값:"+mem1);

        Team team = teamRepository.findByTeamIdxOpt(requestDto.getTeamIdx())
                .orElseThrow(()-> new IllegalArgumentException("해당팀을 찾을 수 없습니다."));
        Mem mem = memRepository.findByMemMstIdx(requestDto.getMemMstIdx())
                .orElseThrow(() -> new IllegalArgumentException("해당팀을 찾을 수 없습니다."));

        boolean alreadyRequested = teamMemberRepository.existsByTeamAndMemAndStatus(team,mem,TeamReqStatus.대기 ) ||
                teamMemberRepository.existsByTeamAndMemAndStatus(team,mem,TeamReqStatus.승인);

        if(alreadyRequested) {
            throw new IllegalArgumentException("이미 해당 팀에 신청한 상태입니다.");
        }

        TeamMember teamMember = TeamMember.createTeamMember(team,mem,RoleType.MEMBER,TeamReqStatus.대기);

        teamMemberRepository.makeTeamMember(teamMember);
    }

}
