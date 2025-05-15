package org.execute.dto.team;

import lombok.Builder;
import lombok.Data;
import org.execute.domain.RoleType;

@Data
@Builder
public class TeamRequestQueryDto {
    private Long teamRequestIdx;
    private String teamNme;
    private String teamDesc;
    private String teamLeaderNme;
    private RoleType roleType;
    private Long teamMemberCount;
    private Long teamIdx;


    public TeamRequestQueryDto(Long teamRequestIdx, String teamNme, String teamDesc, String teamLeaderNme, RoleType roleType, Long teamMemberCount, Long teamIdx) {
        this.teamRequestIdx = teamRequestIdx;
        this.teamNme = teamNme;
        this.teamDesc = teamDesc;
        this.teamLeaderNme = teamLeaderNme;
        this.roleType = roleType;
        this.teamMemberCount = teamMemberCount;
        this.teamIdx = teamIdx;
    }
}
