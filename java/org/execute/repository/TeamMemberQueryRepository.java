package org.execute.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.execute.dto.team.TeamManageDto;
import org.execute.dto.team.TeamMemberQueryDto;
import org.execute.dto.team.TeamRequestQueryDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamMemberQueryRepository {
    @PersistenceContext
    private final EntityManager em;

    public List<TeamMemberQueryDto> getMyTeamList(Long memMstIdx) {
        return em.createQuery(
                        "SELECT DISTINCT new org.execute.dto.team.TeamMemberQueryDto(" +
                                "tm.teamRequestIdx, t.teamNme, t.teamDesc, leader.memNme, tm.roleType) " +
                                "FROM TeamMember tm " +
                                "JOIN tm.team t " +
                                "JOIN t.teamLeader leader " +
                                "WHERE tm.member.memMstIdx = :memMstIdx " +
                                "AND tm.joinStatus = '승인'", TeamMemberQueryDto.class)
                .setParameter("memMstIdx", memMstIdx)
                .getResultList();
    }

    public List<TeamRequestQueryDto> findTeamList() {
        return em.createQuery(
                "SELECT new org.execute.dto.team.TeamRequestQueryDto("+
                            "tm.teamRequestIdx, t.teamNme, t.teamDesc, leader.memNme, tm.roleType, COUNT(tm2), t.teamIdx) " +
                            "FROM TeamMember tm " +
                            "JOIN tm.team t " +
                            "JOIN t.teamLeader leader " +
                            "LEFT JOIN TeamMember tm2 on tm2.team = t AND tm2.joinStatus = '승인' " + //팀원 수 세기
                            "WHERE tm.roleType = 'LEADER' " +
                            "GROUP BY tm.teamRequestIdx, t.teamNme, t.teamDesc, leader.memNme, tm.roleType, t.teamIdx ",
                    TeamRequestQueryDto.class)
                .getResultList();
    }

    public List<TeamManageDto> getMyTeamInfo(Long teamIdx) {
        return em.createQuery(
                        "SELECT new org.execute.dto.team.TeamManageDto(" +
                                "tm.teamRequestIdx, t.teamNme, t.teamDesc, leader.memNme, tm.roleType, " +
                                "(SELECT COUNT(tm2) FROM TeamMember tm2 WHERE tm2.team = t AND tm2.joinStatus = '승인'), " + // 승인된 팀원 수
                                "m.memNme, m.memAct, m.emalTal, m.emalHed) " +
                                "FROM TeamMember tm " +
                                "JOIN tm.team t " +
                                "JOIN tm.member m " +
                                "JOIN t.teamLeader leader " +
                                "WHERE tm.team.teamIdx = :teamIdx " +
                                "AND tm.joinStatus = '승인'",
                        TeamManageDto.class)
                .setParameter("teamIdx", teamIdx)
                .getResultList();
    }


    //public List<TeamMemberQueryDto> getMyTeamList(Long memMstIdx) {
//    return em.createQuery(
//                    "SELECT new org.execute.dto.team.TeamMemberQueryDto(" +
//                            "tm.teamReqeustIdx, t.teamNme, t.teamDesc, leader.memNme, tm.roleType) " +
//                            "FROM TeamMember tm " +
//                            "JOIN tm.team t " + // FETCH 제거
//                            "JOIN t.teamLeader leader " + // FETCH 제거
//                            "WHERE tm.member.memMstIdx = :memMstIdx " +
//                            "AND tm.joinStatus = '승인'", TeamMemberQueryDto.class)
//            .setParameter("memMstIdx", memMstIdx)
//            .getResultList();
}
