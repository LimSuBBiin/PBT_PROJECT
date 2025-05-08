package org.execute.dto.team.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TeamDto {
    private Long teamIdx;
    private String teamNme;
    private String teamDesc;
    private Long teamLeaderIdx; // 팀 리더 아이디
    private String teamLeaderName; // 팀 리더 이름
    private List<TeamMemberDto> teamMembers; // 팀원 목록 (TeamMemberDto를 사용)
    public TeamDto(Long teamIdx, String teamNme, String teamDesc, Long teamLeaderIdx, String teamLeaderName, List<TeamMemberDto> teamMembers) {
        this.teamIdx = teamIdx;
        this.teamNme = teamNme;
        this.teamDesc = teamDesc;
        this.teamLeaderIdx = teamLeaderIdx;
        this.teamLeaderName = teamLeaderName;
        this.teamMembers = teamMembers;
    }
}
