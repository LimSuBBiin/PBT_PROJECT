package org.execute.dto.notice;

import lombok.Data;
import org.execute.domain.Position;

@Data
public class GameReqFoamDto {
    private String memNme;
    private String memHeight; //키
    private Position mainPst; //주 포지션
    private Position subPst; //서브 포지션
    private String memHist;

    public GameReqFoamDto(String memNme, String memHeight, Position mainPst, Position subPst, String memHist) {
        this.memNme = memNme;
        this.memHeight = memHeight;
        this.mainPst = mainPst;
        this.subPst = subPst;
        this.memHist = memHist;
    }
}
