package org.execute.dto.team;

import lombok.Data;
import org.execute.domain.RoleType;
import org.execute.domain.TeamReqStatus;

@Data
public class GetTeamListDto {
    public Long teamIdx;
    public String teamNme;
    public RoleType roleType;
//    public TeamReqStatus joinStatus;

    public GetTeamListDto(Long teamIdx, String teamNme, RoleType roleType) {
        this.teamIdx = teamIdx;
        this.teamNme = teamNme;
        this.roleType = roleType;
    }
}
