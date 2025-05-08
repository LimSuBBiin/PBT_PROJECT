package org.execute.dto.GameRequest;

import lombok.Data;
import org.execute.domain.GameRequestStatus;

@Data
public class FindReqStatusDto {
    private GameRequestStatus gameRequestStatus;

    public FindReqStatusDto(GameRequestStatus gameRequestStatus) {
        this.gameRequestStatus = gameRequestStatus;
    }

    public FindReqStatusDto(){}
}
