package org.execute.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.execute.domain.Team;
import org.execute.dto.team.TeamReqMemDto;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamRepository {
    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public void makeTeam(Team team){
        em.persist(team);
    }

    public Optional<Team> findByTeamNme(String teamNme) {
        try {
            return Optional.ofNullable(
                    em.createQuery("select t from Team t where t.teamNme = :teamNme", Team.class)
                            .setParameter("teamNme", teamNme)
                            .getSingleResult()
            );
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    public Optional<Team> findByTeamIdxOpt(Long teamIdx) {
        try {
            return Optional.ofNullable(
                    em.createQuery("select t from Team t where t.teamIdx = :teamIdx ",
                            Team.class)
                            .setParameter("teamIdx", teamIdx)
                            .getSingleResult()

            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Team findByTeamIdx(Long teamIdx){
        return em.createQuery("select t from Team t where t.teamIdx = :teamIdx", Team.class)
                .setParameter("teamIdx", teamIdx)
                .getSingleResult();
    }
}

//
//    @Transactional
//    public void
