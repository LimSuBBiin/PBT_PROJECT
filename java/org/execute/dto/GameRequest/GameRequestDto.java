package org.execute.dto.GameRequest;

import lombok.Data;
import org.execute.domain.GameRequestStatus;
import org.execute.domain.Position;

@Data
public class GameRequestDto {
    private Long noticeGameIdx;
    private Long teamRequestIdx;
    private GameRequestStatus gameRequestStatus;
    private Position position;



    public GameRequestDto(Long noticeGameIdx, Long teamRequestIdx, GameRequestStatus gameRequestStatus, Position position) {
        this.noticeGameIdx = noticeGameIdx;
        this.teamRequestIdx = teamRequestIdx;
        this.gameRequestStatus = gameRequestStatus;
        this.position = position;

    }

    public GameRequestDto(){}
}
