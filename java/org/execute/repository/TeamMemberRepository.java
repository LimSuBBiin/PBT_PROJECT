package org.execute.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.execute.domain.Mem;
import org.execute.domain.Team;
import org.execute.domain.TeamMember;
import org.execute.domain.TeamReqStatus;
import org.execute.dto.team.TeamManageReqDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamMemberRepository {
    @PersistenceContext
    private final EntityManager em;

    public void makeTeamMember(TeamMember teamMember){
        em.persist(teamMember);
        em.flush();
    }
//
public boolean existsByTeamAndMemAndStatus(Team team, Mem mem, TeamReqStatus status) {
    System.out.println("Team: " + team);
    System.out.println("Mem: " + mem);
    System.out.println("Status: " + status);

    // JPQL 쿼리에서 joinStatus로 필드 이름 수정
    String jpql = "SELECT COUNT(tm) FROM TeamMember tm WHERE tm.team =:team AND tm.member =:member AND tm.joinStatus =:status";

    try {
        Long cnt = em.createQuery(jpql, Long.class)
                .setParameter("team", team)
                .setParameter("member", mem)
                .setParameter("status", status)
                .getSingleResult();
        return cnt > 0;
    } catch (Exception e) {
        System.out.println("쿼리 실행 중 오류: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    //리더 전용 teamIDX의 memMstIdx값 구하기
    public Long findTeamIdByTeamLeaderIdx(Long memMstIdx) {
        return em.createQuery("SELECT t.teamIdx FROM Team t WHERE t.teamLeader.memMstIdx = :memMstIdx", Long.class)
                .setParameter("memMstIdx", memMstIdx)
                .getSingleResult();
    }


//    //회원+리더 포함 teamIdx의 memMstIdx값 구하기
//    public Long findTeamIdByMemMstIdx(Long memMstIdx) {
//        return em.createQuery("SELECT m.tm.teamIdx FROM ")
//    }

    public Optional<TeamMember> findByTmReqIdx(Long teamRequestIdx){
        try{

            return Optional.ofNullable(em.createQuery("SELECT tm FROM TeamMember tm WHERE tm.teamRequestIdx = :teamRequestIdx ", TeamMember.class)
                    .setParameter("teamRequestIdx", teamRequestIdx)
                    .getSingleResult()
            );

        }catch (NoResultException e) {
            return Optional.empty();
        }

    }


    public Optional<Long> findByMemAndTeam(Long teamIdx, Long memMstIdx){
        List<Long> result = em.createQuery(
                        "SELECT tm.teamRequestIdx FROM TeamMember tm WHERE tm.team.teamIdx = :teamIdx AND tm.member.memMstIdx = :memMstIdx", Long.class)
                .setParameter("teamIdx", teamIdx)
                .setParameter("memMstIdx", memMstIdx)
                .getResultList();

        if (result.isEmpty()) {
            System.out.println("❌ 결과 없음 (getResultList 기준)");
            return Optional.empty();
        }

        System.out.println("✅ 조회 성공: " + result.get(0));
        return Optional.of(result.get(0));
    }

//
//    public Optional<TeamManageReqDto> findByTeamIdx(Long teamIdx) {
//        try{
//            return Optional.ofNullable(
//                    em.createQuery("SELECT t from Team t where t.teamIdx = :teamIdx",)
//            )
//        }
//    }
}
