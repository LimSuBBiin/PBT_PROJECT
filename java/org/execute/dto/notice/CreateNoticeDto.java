package org.execute.dto.notice;

import lombok.Builder;
import lombok.Data;
import org.execute.domain.RoleType;

import java.time.LocalDateTime;

@Data
public class CreateNoticeDto {
    private Long memMstIdx; // 작성자 idx
    private Long teamIdx; // 작성자의 팀 idx
    private String noticeTitle;
    private String noticeDesc;
    private String noticeLocation;
    private LocalDateTime noticeGameDate;
    private String cost; //대관 비
    private Long guardCount;
    private Long forwardCount;
    private Long centerCount;
    private Long totalCount;

    public CreateNoticeDto(Long memMstIdx, Long teamIdx, String noticeTitle, String noticeDesc, String noticeLocation, LocalDateTime noticeGameDate, String cost, Long guardCount, Long forwardCount, Long centerCount, Long totalCount) {
        this.memMstIdx = memMstIdx;
        this.teamIdx = teamIdx;
        this.noticeTitle = noticeTitle;
        this.noticeDesc = noticeDesc;
        this.noticeLocation = noticeLocation;
        this.noticeGameDate = noticeGameDate;
        this.cost = cost;
        this.guardCount = guardCount;
        this.forwardCount = forwardCount;
        this.centerCount = centerCount;
        this.totalCount = totalCount;
    }

    public CreateNoticeDto() {
    }
}
