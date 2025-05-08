package org.execute.dto.team;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.execute.domain.RoleType;

@Data
@Builder
public class TeamMemberQueryDto {
    private Long teamRequestIdx;
    private String teamNme;
    private String teamDesc;
    private String teamLeaderNme; //리더 이름
    private RoleType roleType;

    public TeamMemberQueryDto(Long teamRequestIdx, String teamNme, String teamDesc, String teamLeaderNme, RoleType roleType) {
        this.teamRequestIdx = teamRequestIdx;
        this.teamNme = teamNme;
        this.teamDesc = teamDesc;
        this.teamLeaderNme = teamLeaderNme;
        this.roleType = roleType;
    }
}
