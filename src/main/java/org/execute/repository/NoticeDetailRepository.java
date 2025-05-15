package org.execute.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.execute.domain.NoticeGame;
import org.execute.domain.Position;
import org.execute.domain.RoleType;
import org.execute.dto.notice.GameReqFoamDto;
import org.execute.dto.notice.NoticeDetailDto;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class NoticeDetailRepository {
    @PersistenceContext
    private final EntityManager em;
    private final NoticeRepository noticeRepository;


    @Transactional
    public NoticeDetailDto noticeDetail(Long noticeGameIdx) {
        NoticeGame noticeGame;
        try {
            noticeGame = em.createQuery(
                            "SELECT n FROM NoticeGame n " +
                                    "JOIN FETCH n.team t " +
                                    "join FETCH n.mem m " +
                                    "WHERE n.noticeGameIdx = :noticeGameIdx", NoticeGame.class)
                    .setParameter("noticeGameIdx", noticeGameIdx)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new IllegalArgumentException("해당 글이 존재하지 않습니다.");
        }
        Long reqGuardCount = noticeRepository.countPosition(noticeGameIdx, List.of(Position.PG, Position.SG));
        Long reqForwardCount = noticeRepository.countPosition(noticeGameIdx, List.of(Position.SF, Position.PF));
        Long reqCenterCount = noticeRepository.countPosition(noticeGameIdx, List.of(Position.C));

        RoleType roleType = em.createQuery(
                        "SELECT tm.roleType FROM TeamMember tm WHERE tm.member = :mem AND tm.team = :team",
                        RoleType.class
                ).setParameter("mem", noticeGame.getMem())
                .setParameter("team", noticeGame.getTeam())
                .getSingleResult();

        NoticeDetailDto noticeDetailDto = new NoticeDetailDto(
                noticeGame.getNoticeGameIdx(),
                noticeGame.getMemNme(),
                roleType,
                noticeGame.getNoticeTitle(),
                noticeGame.getNoticeDesc(),
                noticeGame.getNoticeLocation(),
                noticeGame.getNoticeGameDate(),
                noticeGame.totalCount(),
                noticeGame.getGuardCount(),
                noticeGame.getForwardCount(),
                noticeGame.getCenterCount(),
                reqGuardCount,
                reqForwardCount,
                reqCenterCount
        );
        return noticeDetailDto;
    }


    @Transactional
    public GameReqFoamDto gameReqFoam(Long memMstIdx) {
        try {
            return em.createQuery(
                            "SELECT new org.execute.dto.notice.GameReqFoamDto(m.memNme, m.memHeight, m.mainPst, m.subPst, m.memHist ) FROM Mem m WHERE m.memMstIdx = :memMstIdx",
                            GameReqFoamDto.class)
                    .setParameter("memMstIdx", memMstIdx)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // 없으면 null 반환
        }
    }


}
