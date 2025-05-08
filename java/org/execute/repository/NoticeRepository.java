package org.execute.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.execute.domain.*;
import org.execute.dto.GameRequest.GameCalendarDto;
import org.execute.dto.notice.NoticeListDto;
import org.execute.dto.notice.NoticeRequestListDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {
    @PersistenceContext
    private final EntityManager em;

    private final TeamMemberQueryRepository teamMemberQueryRepository;
    private final GameRequestRepository gameRequestRepository;

    @Transactional
    public void save(NoticeGame noticeGame) {
        System.out.println("저장 전 noticeIdx 와 TeamIdx"+ noticeGame.getNoticeGameIdx() + noticeGame.getTeam());
        em.persist(noticeGame);
        System.out.println("게시글 저장 완료:" + noticeGame );
        em.flush();
        System.out.println("저장완료");
    }

    public Optional<NoticeGame> findByidx(Long noticeGameIdx){
        try {
            return Optional.ofNullable(
                    em.createQuery("SELECT n from NoticeGame n where n.noticeGameIdx = :noticeGameIdx ", NoticeGame.class)
                    .setParameter("noticeGameIdx" , noticeGameIdx)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<NoticeGame> findByTitle(String noticeTitle){
        try{
            return Optional.ofNullable(
                    em.createQuery("SELECT n FROM NoticeGame n WHERE n.noticeTitle = :noticeTitle ", NoticeGame.class)
                            .setParameter("noticeTitle", noticeTitle)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


    public Long countPosition(Long noticeGameIdx, List<Position> positions) {
        return em.createQuery(
                        "SELECT COUNT(gr) FROM GameRequest gr " +
                                "WHERE gr.noticeGame.noticeGameIdx = :noticeGameIdx " +
                                "AND gr.position IN :positions AND gr.gameRequestStatus = :gameRequestStatus ", Long.class)
                .setParameter("noticeGameIdx", noticeGameIdx)
                .setParameter("positions", positions)
                .setParameter("gameRequestStatus", GameRequestStatus.신청)
                .getSingleResult();

    }

    @Transactional
    public List<NoticeListDto> getNoticeList(Long teamIdx, Long teamRequestIdx) {

        //페이지 입장시 팀에 소속한 멤버인지
        List<TeamReqStatus> validMem = em.createQuery(
                        "SELECT tm.joinStatus FROM TeamMember tm WHERE tm.teamRequestIdx = :teamRequestIdx AND tm.team.teamIdx =:teamIdx", TeamReqStatus.class)
                .setParameter("teamRequestIdx", teamRequestIdx)
                .setParameter("teamIdx", teamIdx)
                .getResultList();
        if (validMem.isEmpty() || validMem.get(0) != TeamReqStatus.승인) {
            throw new RuntimeException("해당 팀에 속한 회원이 아닙니다.");
        }


        System.out.println("valid 통과");


        List<NoticeGame> noticeGames = em.createQuery(
                        "SELECT n FROM NoticeGame n " +
                                "JOIN FETCH n.team t " +
                                "JOIN FETCH n.mem m " +
                                "WHERE t.teamIdx = :teamIdx ", NoticeGame.class)
                .setParameter("teamIdx", teamIdx)
                .getResultList();


        List<NoticeListDto> dtoList = noticeGames.stream().map(n -> {
            Long reqGuardCount = countPosition(n.getNoticeGameIdx(), List.of(Position.PG, Position.SG));
            Long reqForwardCount = countPosition(n.getNoticeGameIdx(), List.of(Position.SF, Position.PF));
            Long reqCenterCount = countPosition(n.getNoticeGameIdx(), List.of(Position.C));
            GameRequestStatus gameRequestStatus = gameRequestRepository.findRequestStatus(n.getNoticeGameIdx(),teamRequestIdx);

            RoleType roleType = em.createQuery(
                            "SELECT tm.roleType FROM TeamMember tm WHERE tm.member = :mem AND tm.team = :team",
                            RoleType.class
                    ).setParameter("mem", n.getMem())
                    .setParameter("team", n.getTeam())
                    .getSingleResult();


            return new NoticeListDto(
                    n.getNoticeGameIdx(),// <- 팀 멤버 idx
                    n.getNoticeGameSeq(),
                    n.getMemNme(),
                    roleType,                  // 혹은 실제 RoleType 조회 필요
                    n.getNoticeTitle(),
                    n.getNoticeLocation(),
                    n.getNoticeGameDate(),
                    n.getGuardCount() + n.getForwardCount() + n.getCenterCount(),
                    n.getGuardCount(),
                    n.getForwardCount(),
                    n.getCenterCount(),
                    reqGuardCount,
                    reqForwardCount,
                    reqCenterCount,
                    n.gameStatusNow(n.getNoticeGameDate()),
                    n.getHit(),
                    gameRequestStatus
            );
        }).collect(Collectors.toList());


    return dtoList;
    }





    @Transactional
    public List<NoticeListDto> getMyNoticeList(Long teamIdx, Long teamRequestIdx, Long memMstIdx){
        //페이지 입장시 팀에 소속한 멤버인지
        TeamReqStatus validMem = em.createQuery(
                        "SELECT tm.joinStatus FROM TeamMember tm WHERE tm.teamRequestIdx = :teamRequestIdx AND tm.team.teamIdx =:teamIdx", TeamReqStatus.class)
                .setParameter("teamRequestIdx", teamRequestIdx)
                .setParameter("teamIdx", teamIdx)
                .getSingleResult();
        if (validMem != TeamReqStatus.승인) {
            throw new RuntimeException("해당 팀에 속한 회원이 아닙니다.");
        }


        System.out.println("valid 통과");

        List<NoticeGame> noticeGames = em.createQuery(
                        "SELECT n FROM NoticeGame n " +
                                "JOIN FETCH n.team t " +
                                "JOIN FETCH n.mem m " +
                                "WHERE t.teamIdx = :teamIdx AND m.memMstIdx = :memMstIdx", NoticeGame.class)
                .setParameter("teamIdx", teamIdx)
                .setParameter("memMstIdx", memMstIdx)
                .getResultList();
        List<NoticeListDto> dtoList = noticeGames.stream().map(n -> {
            Long reqGuardCount = countPosition(n.getNoticeGameIdx(), List.of(Position.PG, Position.SG));
            Long reqForwardCount = countPosition(n.getNoticeGameIdx(), List.of(Position.SF, Position.PF));
            Long reqCenterCount = countPosition(n.getNoticeGameIdx(), List.of(Position.C));
            GameRequestStatus gameRequestStatus = gameRequestRepository.findRequestStatus(n.getNoticeGameIdx(),teamRequestIdx);

            RoleType roleType = em.createQuery(
                            "SELECT tm.roleType FROM TeamMember tm WHERE tm.member = :mem AND tm.team = :team",
                            RoleType.class
                    ).setParameter("mem", n.getMem())
                    .setParameter("team", n.getTeam())
                    .getSingleResult();


            return new NoticeListDto(
                    n.getNoticeGameIdx(),// <- 팀 멤버 idx
                    n.getNoticeGameSeq(),
                    n.getMemNme(),
                    roleType,                  // 혹은 실제 RoleType 조회 필요
                    n.getNoticeTitle(),
                    n.getNoticeLocation(),
                    n.getNoticeGameDate(),
                    n.getGuardCount() + n.getForwardCount() + n.getCenterCount(),
                    n.getGuardCount(),
                    n.getForwardCount(),
                    n.getCenterCount(),
                    reqGuardCount,
                    reqForwardCount,
                    reqCenterCount,
                    n.gameStatusNow(n.getNoticeGameDate()),
                    n.getHit(),
                    gameRequestStatus

            );
        }).collect(Collectors.toList());


        return dtoList;
    }



//    //내가 신청한 경기들 Date가져오는 메소드
//    @Transactional
//    public List<GameCalendarDto> getGameCal(Long memMstIdx) {
//        List<Long> teamRequestIdxList = teamMemberQueryRepository.findMyTeamForNotice(memMstIdx); //승인된 teamRequestIdx 전부 가져옴.
//        List<GameRequest> myGameRequests = em.createQuery(
//                "SELECT gr FROM GameRequest gr " +
//                        "JOIN FETCH gr.teamMember tm " +
//                        "WHERE tm.teamRequestIdx IN (:teamRequestIdxList) " +
//                        "AND gr.gameRequestStatus = :gameRequestStatus", GameRequest.class)
//                .setParameter("teamRequestIdx", teamRequestIdxList)
//                .setParameter("gameRequestStatus", GameRequestStatus.신청)
//                .getResultList();
//        Map<Long, GameRequest> gameRequestMap = myGameRequests.stream().collect(Collectors.toMap(
//                gr -> gr.getNoticeGame().getNoticeGameIdx() , Function.identity()
//        ));
//
//        List<NoticeGame> noticeGames = myGameRequests.stream().map(GameRequest::getNoticeGame).distinct().collect(Collectors.toList());
//
//        List<GameCalendarDto> dtoList = noticeGames.stream().map( n ->
//
//                )
//    }


    //내가 신청한 경기들 notice가져오기.
    @Transactional
    public List<NoticeRequestListDto> getMyReqGameNotice(Long memMstIdx){
        List<Long> teamRequestIdxList = teamMemberQueryRepository.findMyTeamForNotice(memMstIdx); //승인된 teamRequestIdx 전부 가져옴.
        List<GameRequest> myGameRequests = em.createQuery(
                "SELECT gr FROM GameRequest gr " +
                        "JOIN FETCH gr.teamMember tm " +
                        "WHERE tm.teamRequestIdx IN (:teamRequestIdxList) " + "AND gr.gameRequestStatus = :gameRequestStatus", GameRequest.class)
                .setParameter("teamRequestIdxList", teamRequestIdxList)
                .setParameter("gameRequestStatus", GameRequestStatus.신청)
                .getResultList();

        Map<Long, GameRequest> gameRequestMap = myGameRequests.stream()
                .collect(Collectors.toMap(
                    gr -> gr.getNoticeGame().getNoticeGameIdx(),
                        Function.identity()
                ));

        List<NoticeGame> noticeGames = myGameRequests.stream()
                .map(GameRequest::getNoticeGame)
                .distinct()
                .collect(Collectors.toList());

        List<NoticeRequestListDto> dtoList = noticeGames.stream().map(n -> {
            Long reqGuardCount = countPosition(n.getNoticeGameIdx(), List.of(Position.PG, Position.SG));
            Long reqForwardCount = countPosition(n.getNoticeGameIdx(), List.of(Position.SF, Position.PF));
            Long reqCenterCount = countPosition(n.getNoticeGameIdx(), List.of(Position.C));
            GameRequestStatus gameRequestStatus = Optional.ofNullable(gameRequestMap.get(n.getNoticeGameIdx()))
                    .map(GameRequest::getGameRequestStatus)
                    .orElse(null);
            Position position = Optional.ofNullable(gameRequestMap.get(n.getNoticeGameIdx()))
                    .map(GameRequest::getPosition)
                    .orElse(null);

            RoleType roleType = em.createQuery(
                            "SELECT tm.roleType FROM TeamMember tm WHERE tm.member = :mem AND tm.team = :team",
                            RoleType.class
                    ).setParameter("mem", n.getMem())
                    .setParameter("team", n.getTeam())
                    .getSingleResult();


            return new NoticeRequestListDto(
                    n.getNoticeGameIdx(),// <- 팀 멤버 idx
                    n.getNoticeGameSeq(),
                    n.getMemNme(),
                    roleType,                  // 혹은 실제 RoleType 조회 필요
                    n.getNoticeTitle(),
                    n.getNoticeLocation(),
                    n.getNoticeGameDate(),
                    n.getGuardCount() + n.getForwardCount() + n.getCenterCount(),
                    n.getGuardCount(),
                    n.getForwardCount(),
                    n.getCenterCount(),
                    reqGuardCount,
                    reqForwardCount,
                    reqCenterCount,
                    n.gameStatusNow(n.getNoticeGameDate()),
                    n.getHit(),
                    gameRequestStatus,
                    n.getTeam().getTeamNme(),
                    position
            );
        }).collect(Collectors.toList());


        return dtoList;
    }



    @Transactional
    public void noticeHit(Long noticeGameIdx){
        NoticeGame noticeGame = em.find(NoticeGame.class, noticeGameIdx);
        if (noticeGame != null) {
            noticeGame.setHit(noticeGame.getHit() + 1);
            System.out.println("현재 hit 값: " + noticeGame.getHit());
        }
    }

}
