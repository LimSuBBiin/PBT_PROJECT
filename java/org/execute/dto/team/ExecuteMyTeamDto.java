package org.execute.dto.team;

import lombok.Builder;
import lombok.Data;
import org.execute.domain.Gen;
import org.execute.domain.Position;
import org.execute.domain.RoleType;
import org.execute.domain.TeamReqStatus;

@Data
public class ExecuteMyTeamDto {
    private Long teamRequestIdx;
    private Long teamIdx;
    private Long memMstIdx;
    private String teamNme;
    private String memAct;
    private String memNme;
    private String phonHed;
    private String phonBod;
    private String phonTal;
    private String emalHed;
    private String emalTal;
    private RoleType roleType;
    private TeamReqStatus joinStatus;
    private String memHeight;
    private String memWeight;
    private String memHist;
    private Gen memGen;
    private Position mainPst;
    private Position subPst;
    private Long maxMembers;

    @Builder
    public ExecuteMyTeamDto(Long teamRequestIdx, Long teamIdx, Long memMstIdx, String teamNme, String memAct, String memNme, String phonHed, String phonBod, String phonTal, String emalHed, String emalTal, RoleType roleType, TeamReqStatus joinStatus, String memHeight, String memWeight, String memHist, Gen memGen, Position mainPst, Position subPst, Long maxMembers) {
        this.teamRequestIdx = teamRequestIdx;
        this.teamIdx = teamIdx;
        this.memMstIdx = memMstIdx;
        this.teamNme = teamNme;
        this.memAct = memAct;
        this.memNme = memNme;
        this.phonHed = phonHed;
        this.phonBod = phonBod;
        this.phonTal = phonTal;
        this.emalHed = emalHed;
        this.emalTal = emalTal;
        this.roleType = roleType;
        this.joinStatus = joinStatus;
        this.memHeight = memHeight;
        this.memWeight = memWeight;
        this.memHist = memHist;
        this.memGen = memGen;
        this.mainPst = mainPst;
        this.subPst = subPst;
        this.maxMembers = maxMembers;
    }
}
