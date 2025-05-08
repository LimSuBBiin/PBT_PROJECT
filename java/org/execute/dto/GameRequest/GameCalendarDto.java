package org.execute.dto.GameRequest;

import lombok.Data;
import org.execute.domain.GameRequestStatus;
import org.execute.domain.Position;

import java.time.LocalDateTime;

@Data
public class GameCalendarDto {
    private Long gameRequestIdx;
    private Long noticeGameIdx;
    private String noticeTitle;
    private GameRequestStatus gameRequestStatus;
    private Position position;
    private String noticeLocation;
    private LocalDateTime noticeGameDate;
    private String cost;
    private String memNme;

    public GameCalendarDto(Long gameRequestIdx, Long noticeGameIdx, String noticeTitle, GameRequestStatus gameRequestStatus, Position position, String noticeLocation, LocalDateTime noticeGameDate, String cost, String memNme) {
        this.gameRequestIdx = gameRequestIdx;
        this.noticeGameIdx = noticeGameIdx;
        this.noticeTitle = noticeTitle;
        this.gameRequestStatus = gameRequestStatus;
        this.position = position;
        this.noticeLocation = noticeLocation;
        this.noticeGameDate = noticeGameDate;
        this.cost = cost;
        this.memNme = memNme;
    }

    public GameCalendarDto() {

    }
}
