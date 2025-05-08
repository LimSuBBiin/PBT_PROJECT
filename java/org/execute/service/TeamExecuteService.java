package org.execute.service;

import lombok.RequiredArgsConstructor;
import org.execute.domain.TeamMember;
import org.execute.dto.team.ExecuteMyTeamDto;
import org.execute.dto.team.GetTeamListDto;
import org.execute.repository.MemRepository;
import org.execute.repository.TeamMemberQueryRepository;
import org.execute.repository.TeamMemberRepository;
import org.execute.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class TeamExecuteService {
    private final TeamMemberQueryRepository teamMemberQueryRepository;
    private final MemRepository memRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    public List<ExecuteMyTeamDto> getMyTeamMemberList(Long teamIdx, Long teamLeaderIdx){

        return teamMemberQueryRepository.getMyTeamMemberList(teamIdx, teamLeaderIdx);
    }

    public List<ExecuteMyTeamDto> getJoinExecuteList(Long teamIdx, Long memMstIdx){
        return teamMemberQueryRepository.getJoinExecuteList(teamIdx,memMstIdx);
    }

    public Long findTeamIdxByLeaderIdx(Long memMstIdx) {
        return teamMemberRepository.findTeamIdByTeamLeaderIdx(memMstIdx);
    }

    public void approveMem(Long teamRequestIdx, Long teamLeaderIdx, Long teamIdx){
        Optional<TeamMember> teamMember = teamMemberRepository.findByTmReqIdx(teamRequestIdx);
        if(teamMember.isEmpty()){
            throw new RuntimeException("신청하지 않거나 ,존재하지 않는 회원입니다.");
        } else {
            teamMemberQueryRepository.approveMember(teamRequestIdx, teamLeaderIdx, teamIdx);
        }
    }

    public void rejectMem(Long teamRequestIdx, Long teamLeaderIdx, Long teamIdx) {
        Optional<TeamMember> teamMember = teamMemberRepository.findByTmReqIdx(teamRequestIdx);
        if(teamMember.isEmpty()){
            throw new RuntimeException("신청하지 않거나 ,존재하지 않는 회원입니다.");
        } else {
            teamMemberQueryRepository.rejectMember(teamRequestIdx, teamLeaderIdx, teamIdx);
        }

    }



}
