package org.execute.dto.team;

import lombok.Builder;
import lombok.Data;
import org.execute.domain.TeamReqStatus;

@Data
@Builder
public class TeamManageReqDto {
    private Long teamRequestIdx;
    private Long teamIdx;
    private String memNme;
    private String memAct;
    private String emalTal;
    private String emalHed;
    private TeamReqStatus joinStatus;

    public TeamManageReqDto(Long teamRequestIdx, Long teamIdx, String memNme, String memAct, String emalTal, String emalHed, TeamReqStatus joinStatus) {
        this.teamRequestIdx = teamRequestIdx;
        this.teamIdx = teamIdx;
        this.memNme = memNme;
        this.memAct = memAct;
        this.emalTal = emalTal;
        this.emalHed = emalHed;
        this.joinStatus = joinStatus;
    }
}
