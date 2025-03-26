package org.execute.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "team_member_tbl")
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_request_idx")
    private Long teamRequestIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_idx", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_mst_idx", nullable = false)
    private Mem member;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType = RoleType.MEMBER; // 팀 역할 (Leader/ Member)

    @Enumerated(EnumType.STRING)
    @Column(name = "join_status", nullable = false)
    private TeamReqStatus joinStatus; // 가입 상태 (승인 / 대기)

    @CreationTimestamp
    @Column(name = "reg_dt", updatable = false)
    private LocalDateTime regDt; // 현재 시간 자동 설정


    //연관관계 메소드
    public void setTeam(Team team){
        this.team =team;
    }

    public void setMember(Mem member){
        this.member = member;
    }

    public static TeamMember createTeamMember(Team team, Mem member, RoleType roleType, TeamReqStatus reqStatus) {
        TeamMember teamMember = new TeamMember();
        teamMember.setTeam(team);  // team을 설정
        teamMember.setMember(member);
        teamMember.setRoleType(roleType);
        teamMember.setJoinStatus(reqStatus);
        return teamMember;
    }


    /**
     *
     * 승인 거절
     *
     */




}
