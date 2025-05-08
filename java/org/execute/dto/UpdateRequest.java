package org.execute.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.execute.domain.Gen;
import org.execute.domain.Position;


@Getter
public class UpdateRequest {
    private Long memMstIdx;
    private String memNme;
    private String phonHed;
    private String phonBod;
    private String phonTal;
    private String emalTal;
    private String emalHed;
    private Gen memGen;
    private String memHeight;
    private Position mainPst;
    private Position subPst;
    private String memWeight;
    private String memHist;

    public void setMemNme(String memNme) {
        this.memNme = memNme;
    }

    public void setMemMstIdx(Long memMstIdx) {
        this.memMstIdx = memMstIdx;
    }

    public void setEmalTal(String emalTal) {
        this.emalTal = emalTal;
    }

    public void setEmalHed(String emalHed) {
        this.emalHed = emalHed;
    }

    public void setPhonHed(String phonHed) {
        this.phonHed = phonHed;
    }

    public void setPhonBod(String phonBod) {
        this.phonBod = phonBod;
    }

    public void setPhonTal(String phonTal) {
        this.phonTal = phonTal;
    }

    public void setMemGen(Gen memGen) {this.memGen = memGen; }

    public void setMemHeight(String memHeight) {
        this.memHeight = memHeight;
    }

    public void setMainPst(Position mainPst) {
        this.mainPst = mainPst;
    }

    public void setSubPst(Position subPst) {
        this.subPst = subPst;
    }

    public void setMemWeight(String memWeight) {
        this.memWeight = memWeight;
    }

    public void setMemHist(String memHist) {
        this.memHist = memHist;
    }

    @Builder
    public UpdateRequest(Long memMstIdx, String memNme, String phonHed, String phonBod, String phonTal, String emalTal, String emalHed, Gen memGen, String memWeight, String memHeight,Position mainPst, Position subPst, String memHist) {
        this.memMstIdx = memMstIdx;
        this.memNme = memNme;
        this.phonHed = phonHed;
        this.phonBod = phonBod;
        this.phonTal = phonTal;
        this.emalTal = emalTal;
        this.emalHed = emalHed;
        this.memGen = memGen;
        this.memHeight = memHeight;
        this.memWeight = memWeight;
        this.mainPst = mainPst;
        this.subPst = subPst;
        this.memHist = memHist;
    }

    //기본생성자
    public UpdateRequest() {}
}
