package org.execute.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "team_tbl")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 적용
    @Column(name = "TEAM_IDX")
    private Long teamIdx;

    @Column(name = "TEAM_NME", nullable = false, length = 30)
    private String teamNme;

    @Column(name = "TEAM_DESC")
    private String teamDesc;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TEAM_LEADER_IDX", nullable = false)
    private Mem teamLeader;

    @JsonIgnore
    @OneToMany(mappedBy = "team")
    private List<TeamMember> teamMembers = new ArrayList<>(); // 팀원 관계 (1:N)



    //연관관계 정리
    public void setMem(Mem mem){
        this.teamLeader = mem;
    }


    // 팀 생성 시, 팀 리더의 roleType을 LEADER로 설정하고, 팀 리더를 TeamMember에 추가
    public static Team createTeam(Mem teamLeader, String teamNme, String teamDesc) {
        Team team = Team.builder()
                .teamLeader(teamLeader)
                .teamNme(teamNme)
                .teamDesc(teamDesc)
                .build();

        //팀 리더를 팀원으로 추가
        TeamMember leaderMember = TeamMember.createTeamMember(team,teamLeader,RoleType.LEADER, TeamReqStatus.승인);
        return team;
    }



//
//    public static Team createTeam(Mem mem, )

}
