package org.execute.dto.notice;

import lombok.Data;
import org.execute.domain.RoleType;

import java.time.LocalDateTime;

@Data
public class NoticeDetailDto {
    private Long NoticeGameIdx;
    private String memNme; // 작성자이름
    private RoleType roleType;
    private String noticeTitle;
    private String noticeDesc;
    private String noticeLocation;
    private LocalDateTime noticeGameDate;
    private Long totalCount;
    private Long guardCount;
    private Long forwardCount;
    private Long centerCount;
    private Long reqGuardCnt;
    private Long reqForwardCnt;
    private Long reqCenterCnt;

    public NoticeDetailDto(Long noticeGameIdx, String memNme, RoleType roleType, String noticeTitle, String noticeDesc, String noticeLocation, LocalDateTime noticeGameDate, Long totalCount, Long guardCount, Long forwardCount, Long centerCount, Long reqGuardCnt, Long reqForwardCnt, Long reqCenterCnt) {
        NoticeGameIdx = noticeGameIdx;
        this.memNme = memNme;
        this.roleType = roleType;
        this.noticeTitle = noticeTitle;
        this.noticeDesc = noticeDesc;
        this.noticeLocation = noticeLocation;
        this.noticeGameDate = noticeGameDate;
        this.totalCount = totalCount;
        this.guardCount = guardCount;
        this.forwardCount = forwardCount;
        this.centerCount = centerCount;
        this.reqGuardCnt = reqGuardCnt;
        this.reqForwardCnt = reqForwardCnt;
        this.reqCenterCnt = reqCenterCnt;
    }

    public NoticeDetailDto() {}
}
