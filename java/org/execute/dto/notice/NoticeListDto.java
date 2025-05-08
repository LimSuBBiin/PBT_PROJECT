package org.execute.dto.notice;

import lombok.Builder;
import lombok.Data;
import org.execute.domain.GameRequestStatus;
import org.execute.domain.GameStatus;
import org.execute.domain.RoleType;

import java.time.LocalDateTime;

@Data
public class NoticeListDto {

//    public Long memMstIdx; // 작성자 idx noticeGameSeq가져와야할듯?
    private Long noticeGameIdx; //게시판 idx 리스트화
    private Long noticeGameSeq;
    private String memNme; // 작성자
    private RoleType roleType; // 역할
    private String noticeTitle;
    private String noticeLocation; //경기 위치
    private LocalDateTime noticeGameDate;
    private Long totalCount;
    private Long guardCount; //모집 중인 가드 수
    private Long forwardCount; //
    private Long centerCount;
    private Long reqGuardCnt;
    private Long reqForwardCnt;
    private Long reqCenterCnt;
    private GameStatus gameStatus;
    private Long hit;
    private GameRequestStatus gameRequestStatus;
//    private Long currentGuardCnt; // 현재 모인 가드 수
//    private Long currentForwardCnt; // 현재 모인 포워드 수
//    private Long currentCenterCnt; // 현재 모인 센터 수
//
//
    public NoticeListDto(Long noticeGameIdx, Long noticeGameSeq,String memNme, RoleType roleType, String noticeTitle, String noticeLocation, LocalDateTime noticeGameDate, Long totalCount, Long guardCount, Long forwardCount, Long centerCount, Long reqGuardCnt, Long reqForwardCnt, Long reqCenterCnt, GameStatus gameStatus, Long hit, GameRequestStatus gameRequestStatus) {
//        this.memMstIdx = memMstIdx;
        this.noticeGameIdx = noticeGameIdx;
        this.noticeGameSeq = noticeGameSeq;
        this.memNme = memNme;
        this.roleType = roleType;
        this.noticeTitle = noticeTitle;
        this.noticeLocation = noticeLocation;
        this.noticeGameDate = noticeGameDate;
        this.totalCount = totalCount;
        this.guardCount = guardCount;
        this.forwardCount = forwardCount;
        this.centerCount = centerCount;
        this.reqGuardCnt = reqGuardCnt;
        this.reqForwardCnt = reqForwardCnt;
        this.reqCenterCnt = reqCenterCnt;
        this.gameStatus = gameStatus;
        this.hit = hit;
        this.gameRequestStatus = gameRequestStatus;
    }

    public NoticeListDto() {}
}
