package org.execute.dto.team;

import lombok.Builder;
import lombok.Data;
import org.execute.domain.RoleType;
import org.execute.domain.TeamReqStatus;

@Data
@Builder
public class TeamManageDto {
    private Long teamRequestIdx;
    private String memNme;
    private String memAct;
    private String emalTal;
    private String emalHed;
    private String teamNme;
    private String teamDesc;
    private String teamLeaderNme;
    private RoleType roleType;
    private Long teamMemberCount;
    private TeamReqStatus joinStatus;

    public TeamManageDto(Long teamRequestIdx, String memNme, String memAct, String emalTal, String emalHed, String teamNme, String teamDesc, String teamLeaderNme, RoleType roleType, Long teamMemberCount, TeamReqStatus joinStatus) {
        this.teamRequestIdx = teamRequestIdx;
        this.memNme = memNme;
        this.memAct = memAct;
        this.emalTal = emalTal;
        this.emalHed = emalHed;
        this.teamNme = teamNme;
        this.teamDesc = teamDesc;
        this.teamLeaderNme = teamLeaderNme;
        this.roleType = roleType;
        this.teamMemberCount = teamMemberCount;
        this.joinStatus = joinStatus;
    }
}
