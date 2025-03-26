package org.execute.dto.team.request;

import lombok.Builder;
import lombok.Getter;
import org.execute.domain.RoleType;
import org.execute.domain.TeamReqStatus;

@Getter
@Builder
public class TeamMemberDto {
    private Long memMstIdx;
    private String memNme;
    private RoleType roleType;
    private TeamReqStatus joinStatus;


    public TeamMemberDto(Long memMstIdx, String memNme, RoleType roleType, TeamReqStatus joinStatus) {
        this.memMstIdx = memMstIdx;
        this.memNme = memNme;
        this.roleType = roleType;
        this.joinStatus = joinStatus;
    }
}

