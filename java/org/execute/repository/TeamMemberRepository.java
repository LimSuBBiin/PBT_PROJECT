package org.execute.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.execute.domain.Mem;
import org.execute.domain.Team;
import org.execute.domain.TeamMember;
import org.execute.domain.TeamReqStatus;
import org.execute.dto.team.TeamManageReqDto;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamMemberRepository {
    @PersistenceContext
    private final EntityManager em;

    public void makeTeamMember(TeamMember teamMember){
        em.persist(teamMember);
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

//
//    public Optional<TeamManageReqDto> findByTeamIdx(Long teamIdx) {
//        try{
//            return Optional.ofNullable(
//                    em.createQuery("SELECT t from Team t where t.teamIdx = :teamIdx",)
//            )
//        }
//    }
}
