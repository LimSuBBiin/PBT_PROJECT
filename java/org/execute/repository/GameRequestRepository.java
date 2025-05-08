package org.execute.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.execute.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GameRequestRepository {
    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public void save(GameRequest gameRequest){
        em.persist(gameRequest);
        em.flush();
    }


    public boolean existByTeamMemberAndNotice(TeamMember teamMember, NoticeGame noticeGame){
        System.out.println("Tm: "+teamMember);
        System.out.println("Notice: "+noticeGame);

        String jpql = "SELECT COUNT(gr) FROM GameRequest gr WHERE gr.teamMember = :teamMember AND gr.noticeGame = :noticeGame ";


            Long cnt = em.createQuery(jpql, Long.class)
                    .setParameter("teamMember", teamMember)
                    .setParameter("noticeGame", noticeGame)
                    .getSingleResult();
            return cnt > 0;

    }

    @Transactional
    public void cancelGameRequest(Long noticeGameIdx, Long teamRequestIdx) {
            GameRequest gameRequest = em.createQuery(
                            "SELECT g FROM GameRequest g WHERE g.noticeGame.noticeGameIdx = :noticeGameIdx AND g.teamMember.teamRequestIdx = : teamRequestIdx "
                            , GameRequest.class)
                    .setParameter("noticeGameIdx", noticeGameIdx)
                    .setParameter("teamRequestIdx", teamRequestIdx)
                    .getSingleResult();

        if(gameRequest == null) {
            throw new RuntimeException("해당 정보를 가진 회원을 찾을 수 없습니다.");
        }
            gameRequest.cancel();
            em.merge(gameRequest);
            System.out.println("해당 사용자가 경기를 취소하였습니다.");
    }

    @Transactional
    public void reJoinGameRequest(Long noticeGameIdx, Long teamRequestIdx, Position position){
        GameRequest gameRequest = em.createQuery(
                "SELECT g FROM GameRequest g WHERE g.noticeGame.noticeGameIdx = :noticeGameIdx AND g.teamMember.teamRequestIdx = :teamRequestIdx ", GameRequest.class)
                .setParameter("noticeGameIdx", noticeGameIdx)
                .setParameter("teamRequestIdx", teamRequestIdx)
                .getSingleResult();

        if(gameRequest == null) {
            throw new RuntimeException("해당 정보를 가진 회원을 찾을 수 없습니다.");
        }

        if(gameRequest.getGameRequestStatus() == GameRequestStatus.신청) {
            throw new RuntimeException("이미 해당 경기에 신청한 회원입니다.");
        }
        gameRequest.reJoin();
        gameRequest.setPosition(position);
        em.merge(gameRequest);
        System.out.println("해당 경기 재신청 완료");

    }

    @Transactional
    public GameRequestStatus findRequestStatus(Long noticeGameIdx, Long teamRequestIdx) {
        try {
            return em.createQuery(
                            "SELECT g.gameRequestStatus FROM GameRequest g WHERE g.noticeGame.noticeGameIdx = :noticeGameIdx " +
                                    "AND g.teamMember.teamRequestIdx = :teamRequestIdx", GameRequestStatus.class
                    )
                    .setParameter("noticeGameIdx", noticeGameIdx)
                    .setParameter("teamRequestIdx", teamRequestIdx)
                    .getSingleResult();
        } catch (NoResultException e) {
            // 참여하지 않은 경우 null 리턴
            return GameRequestStatus.미신청;
        }
    }

    @Transactional
    public Optional<GameRequest> findExistJoin (Long noticeGameIdx, Long teamRequestIdx){
        try {
            return Optional.ofNullable(
                    em.createQuery(
                    "SELECT g FROM GameRequest g WHERE g.noticeGame.noticeGameIdx = :noticeGameIdx AND g.teamMember.teamRequestIdx = :teamRequestIdx "
                            , GameRequest.class)
                    .setParameter("noticeGameIdx", noticeGameIdx)
                    .setParameter("teamRequestIdx", teamRequestIdx)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }



}
