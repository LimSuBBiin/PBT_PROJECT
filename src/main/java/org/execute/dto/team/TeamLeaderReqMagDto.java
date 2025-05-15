package org.execute.dto.team;


import lombok.Builder;
import lombok.Data;
import org.execute.domain.RoleType;
import org.execute.domain.TeamReqStatus;
@Data
@Builder
public class TeamLeaderReqMagDto {
    private Long teamRequestIdx;
    private Long teamIdx;
    private String teamLeaderNme;
    private String teamNme;
    private String memMstIdx;
    private String memNme;
    private RoleType roleType;
    private TeamReqStatus joinStatus;

    public TeamLeaderReqMagDto(Long teamRequestIdx, Long teamIdx, String teamLeaderNme, String teamNme, String memMstIdx, String memNme, RoleType roleType, TeamReqStatus joinStatus) {
        this.teamRequestIdx = teamRequestIdx;
        this.teamIdx = teamIdx;
        this.teamLeaderNme = teamLeaderNme;
        this.teamNme = teamNme;
        this.memMstIdx = memMstIdx;
        this.memNme = memNme;
        this.roleType = roleType;
        this.joinStatus = joinStatus;
    }
}
