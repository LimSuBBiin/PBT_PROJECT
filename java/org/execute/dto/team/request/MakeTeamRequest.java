package org.execute.dto.team.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MakeTeamRequest {
    private Long teamLeaderIdx;
    private String teamNme;
    private String teamDesc;

    public void setTeamLeaderIdx(Long teamLeaderIdx) {
        this.teamLeaderIdx = teamLeaderIdx;
    }

    public void setTeamNme(String teamNme) {
        this.teamNme = teamNme;
    }

    public void setTeamDesc(String teamDesc) {
        this.teamDesc = teamDesc;
    }

    public MakeTeamRequest(Long teamLeaderIdx, String teamNme, String teamDesc) {
        this.teamLeaderIdx = teamLeaderIdx;
        this.teamNme = teamNme;
        this.teamDesc = teamDesc;
    }

    public MakeTeamRequest() {
        // 기본 생성자 추가
    }
}
