package org.execute.dto.team;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class TeamReqMemDto {
    private Long teamIdx;
    private Long memMstIdx;

    public TeamReqMemDto(Long teamIdx, Long memMstIdx) {
        this.teamIdx = teamIdx;
        this.memMstIdx = memMstIdx;
    }


    public TeamReqMemDto() {

    }
}
