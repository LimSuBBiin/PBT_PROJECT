package org.execute.dto.team.request;


import lombok.Builder;
import lombok.Getter;
import org.execute.domain.TeamReqStatus;

@Getter
@Builder
public class TeamApproveReqDto {
    private Long teamRequestIdx;
    private Long teamIdx;
    private Long memMstIdx;
    private TeamReqStatus joinStatus;

    public TeamApproveReqDto(Long teamRequestIdx, Long teamIdx, Long memMstIdx, TeamReqStatus joinStatus) {
        this.teamRequestIdx = teamRequestIdx;
        this.teamIdx = teamIdx;
        this.memMstIdx = memMstIdx;
        this.joinStatus = joinStatus;
    }
}
