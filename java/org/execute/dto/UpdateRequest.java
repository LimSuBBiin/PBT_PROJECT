package org.execute.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Builder
public class UpdateRequest {
    private Long memMstIdx;
    private String memNme;
    private String phonHed;
    private String phonBod;
    private String phonTal;
    private String emalTal;
    private String emalHed;

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

    public UpdateRequest(Long memMstIdx, String memNme, String phonHed, String phonBod, String phonTal, String emalTal, String emalHed) {
        this.memMstIdx = memMstIdx;
        this.memNme = memNme;
        this.phonHed = phonHed;
        this.phonBod = phonBod;
        this.phonTal = phonTal;
        this.emalTal = emalTal;
        this.emalHed = emalHed;
    }

    //기본생성자
    public UpdateRequest() {}
}
