package org.execute.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.execute.domain.Mem;
import org.execute.domain.TeamMember;
import org.execute.domain.TeamReqStatus;
import org.execute.dto.team.*;
import org.execute.dto.team.request.TeamApproveReqDto;
import org.springframework.stereotype.Repository;

import java.nio.file.AccessDeniedException;
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


//    public List<TeamLeaderReqMagDto> joinMyMember() {
//        return em.createQuery(
//                "SELECT new org.execute.dto.team.TeamLeaderReqMagDto("+
//                        "tm.teamRequestIdx, t.teamNme, leader.memNme, tm.roleType, tm.joinStatus, t.teamIdx, m.memMstIdx, m.memNme) " +
//                        "FROM TeamMember tm" +
//                        "JOIN tm.team t " +
//                        "JOIN tm.member m" +
//                        "WHERE tm.joinStatus = '대기' " +
//                        "GRUOP BY tm.teamRequestIdx, t.teamNme, t.teamDesc"
//        )
//    }

    
    
//팀원 관리 메소드
    public List<ExecuteMyTeamDto> getMyTeamMemberList(Long teamIdx, Long memMstIdx) {

        Long teamLeaderIdx = em.createQuery(
                "SELECT t.teamLeader.memMstIdx FROM Team t WHERE t.teamIdx = :teamIdx", Long.class)
                .setParameter("teamIdx", teamIdx)
                .getSingleResult();

        if(!teamLeaderIdx.equals(memMstIdx)){
            throw new RuntimeException("리더만 접근 가능합니다.");
        }

        // 팀에 속한 멤버 수 계산
        Long maxMembers = em.createQuery(
                        "SELECT COUNT(tm.member) FROM TeamMember tm WHERE tm.team.teamIdx = :teamIdx AND tm.joinStatus = :joinStatus", Long.class)
                .setParameter("teamIdx", teamIdx)
                .setParameter("joinStatus", TeamReqStatus.승인)
                .getSingleResult();

        // 팀 멤버 리스트 가져오기
        return em.createQuery(
                        "SELECT new org.execute.dto.team.ExecuteMyTeamDto(" +
                                "tm.teamRequestIdx, t.teamIdx, m.memMstIdx, t.teamNme, m.memAct, m.memNme, m.phonHed, m.phonBod, m.phonTal, m.emalHed, m.emalTal, tm.roleType, tm.joinStatus, m.memHeight, m.memWeight, m.memHist, m.memGen, m.mainPst, m.subPst, :maxMembers" +
                                ") " +
                                "FROM TeamMember tm " +
                                "JOIN tm.team t " +
                                "JOIN tm.member m " +
                                "WHERE t.teamIdx = :teamIdx " +
                                "AND tm.joinStatus = :joinStatus", ExecuteMyTeamDto.class)
                .setParameter("teamIdx", teamIdx)
                .setParameter("maxMembers", maxMembers)
                .setParameter("joinStatus", TeamReqStatus.승인)
                .getResultList();
    }

    public List<ExecuteMyTeamDto> getJoinExecuteList(Long teamIdx, Long memMstIdx){
        Long teamLeaderIdx = em.createQuery(
                        "SELECT t.teamLeader.memMstIdx FROM Team t WHERE t.teamIdx = :teamIdx", Long.class)
                .setParameter("teamIdx", teamIdx)
                .getSingleResult();

        if(!teamLeaderIdx.equals(memMstIdx)){
            throw new RuntimeException("리더만 접근 가능합니다.");
        }

        // 팀에 속한 멤버 수 계산
        Long maxMembers = em.createQuery(
                        "SELECT COUNT(tm.member) FROM TeamMember tm WHERE tm.team.teamIdx = :teamIdx AND tm.joinStatus = :joinStatus", Long.class)
                .setParameter("teamIdx", teamIdx)
                .setParameter("joinStatus", TeamReqStatus.대기)
                .getSingleResult();


        return em.createQuery(
                        "SELECT new org.execute.dto.team.ExecuteMyTeamDto(" +
                                "tm.teamRequestIdx, t.teamIdx, m.memMstIdx, t.teamNme, m.memAct, m.memNme, m.phonHed, m.phonBod, m.phonTal, m.emalHed, m.emalTal, tm.roleType, tm.joinStatus, m.memHeight, m.memWeight, m.memHist, m.memGen, m.mainPst, m.subPst, :maxMembers "  +
                                ") " +
                                "FROM TeamMember tm " +
                                "JOIN tm.team t " +
                                "JOIN tm.member m " +
                                "WHERE t.teamIdx = :teamIdx " +
                                "AND tm.joinStatus = :joinStatus", ExecuteMyTeamDto.class)
                .setParameter("teamIdx", teamIdx)
                .setParameter("maxMembers", maxMembers)
                .setParameter("joinStatus", TeamReqStatus.대기)
                .getResultList();
    }
    @Transactional
    public void approveMember(Long teamLeaderIdx, Long teamRequestIdx, Long teamIdx) {
                Long checkLeaderIdx = em.createQuery(
                "SELECT t.teamLeader.memMstIdx FROM Team t WHERE t.teamIdx = :teamIdx", Long.class)
                .setParameter("teamIdx", teamIdx)
                .getSingleResult();

        if(!checkLeaderIdx.equals(teamLeaderIdx)){
           throw new RuntimeException("리더만 접근 가능합니다.");
        }

        TeamMember teamMember = em.find(TeamMember.class, teamRequestIdx);
        if(teamMember == null) {
            throw new RuntimeException("팀 신청에서 사용자를 찾을 수 없습니다.");
        }

        teamMember.approve();
        em.merge(teamMember);
        System.out.println("해당 사용자를 성공적으로 승인하였습니다.");
    }

    @Transactional
    public void rejectMember(Long teamLeaderIdx, Long teamRequestIdx, Long teamIdx) {
        Long checkLeaderIdx = em.createQuery(
                        "SELECT t.teamLeader.memMstIdx FROM Team t WHERE t.teamIdx = :teamIdx", Long.class)
                .setParameter("teamIdx", teamIdx)
                .getSingleResult();

        if(!checkLeaderIdx.equals(teamLeaderIdx)){
            throw new RuntimeException("리더만 접근 가능합니다.");
        }

        TeamMember teamMember = em.find(TeamMember.class, teamRequestIdx);
        if(teamMember == null) {
            throw new RuntimeException("팀 신청에서 사용자를 찾을 수 없습니다.");
        }

        teamMember.reject();
        em.merge(teamMember);
        System.out.println("해당 사용자를 거절하였습니다.");
    }



//    public List<TeamApproveReqDto> approveMem(Long teamLeaderIdx, Long memMstIdx, Long teamIdx) {
//        Long checkLeaderIdx = em.createQuery(
//                "SELECT t.teamLeader.memMstIdx FROM Team t WHERE t.teamIdx = :teamIdx", Long.class)
//                .setParameter("teamIdx", teamIdx)
//                .getSingleResult();
//
//        if(!checkLeaderIdx.equals(teamLeaderIdx)){
//           throw new RuntimeException("리더만 접근 가능합니다.");
//        }
//
//        return em.createQuery("SELECT new org.execute.dto.team.request.TeamApproveReqDto(" +
//                "tm.teamRequestIdx, t.teamIdx, m.memMstIdx, tm.joinStatus ") +
//                "FROM teamMember tm " +
//                "JOIN tm.team t " +
//                "JOIN tm.member m " +
//
//                )
//    }

//여기까지 팀원 관리 메소드

    @Transactional
    public List<GetTeamListDto> findMyTeamList(Long memMstIdx){
        return em.createQuery("SELECT new org.execute.dto.team.GetTeamListDto(t.teamIdx, t.teamNme, tm.roleType ) " +
                                "FROM Mem m " +
                                "JOIN m.teamMembers tm " +
                                "JOIN tm.team t " +
                                "WHERE m.memMstIdx = :memMstIdx " +
                                "AND tm.joinStatus = :joinStatus",
                        GetTeamListDto.class)
                .setParameter("memMstIdx", memMstIdx)
                .setParameter("joinStatus", TeamReqStatus.승인)
                .getResultList();
    }

//    @Transactional
//    public List<TeamMember> findMyTeamForNotice(Long memMstIdx){
//        List<TeamMember> teamMembers = em.createQuery("SELECT t.teamIdx, t.teamNme " +
//                "FROM TeamMember tm " +
//                "JOIN tm.member m " +
//                "JOIN tm.team t " +
//                "WHERE m.memMstIdx = :memMstIdx " +
//                "AND tm.joinStatus = :joinStatus",
//                TeamMember.class)
//                .setParameter("memMstIdx", memMstIdx)
//                .setParameter("joinStatus", TeamReqStatus.승인)
//                .getResultList();
//        return teamMembers;
//    }

    @Transactional
    public List<Long> findMyTeamForNotice(Long memMstIdx) {
        List<Long> teamRequestIdx = em.createQuery("SELECT tm.teamRequestIdx FROM TeamMember tm " +
                                                        "JOIN tm.member m " +
                "WHERE m.memMstIdx = :memMstIdx " +
                "AND tm.joinStatus = :joinStatus", Long.class)
                .setParameter("memMstIdx", memMstIdx)
                .setParameter("joinStatus", TeamReqStatus.승인)
                .getResultList();
       return teamRequestIdx;
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
