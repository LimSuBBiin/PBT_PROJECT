package org.execute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class MemDto {
    private Long memMstIdx;
    private Long memSeq;
    private String memAct;
    private String memPwd;
    private String memNme;
    private String phonHed;
    private String phonBod;
    private String phonTal;
    private String emalTal;
    private String emalHed;
    private String memGen;
    private String memRole;

    public void setEmalHed(String emalHed) {
        this.emalHed = emalHed;
    }

    public void setMemMstIdx(Long memMstIdx) {
        this.memMstIdx = memMstIdx;
    }

    public void setMemSeq(Long memSeq) {
        this.memSeq = memSeq;
    }

    public void setMemAct(String memAct) {
        this.memAct = memAct;
    }

    public void setMemPwd(String memPwd) {
        this.memPwd = memPwd;
    }

    public void setPhonHed(String phonHed) {
        this.phonHed = phonHed;
    }

    public void setPhonBod(String phonBod) {
        this.phonBod = phonBod;
    }

    public void setMemNme(String memNme) {
        this.memNme = memNme;
    }

    public void setPhonTal(String phonTal) {
        this.phonTal = phonTal;
    }

    public void setEmalTal(String emalTal) {
        this.emalTal = emalTal;
    }

    public void setMemGen(String memGen) {
        this.memGen = memGen;
    }

    public void setMemRole(String memRole) {
        this.memRole = memRole;
    }

    // 기본 생성자 추가
    public MemDto() {}

    public MemDto(Long memMstIdx, Long memSeq, String memAct, String memPwd, String memNme, String phonHed, String phonBod, String phonTal, String emalTal, String emalHed,String memGen, String memRole) {
        this.memMstIdx = memMstIdx;
        this.memSeq = memSeq;
        this.memAct = memAct;
        this.memPwd = memPwd;
        this.memNme = memNme;
        this.phonHed = phonHed;
        this.phonBod = phonBod;
        this.phonTal = phonTal;
        this.emalTal = emalTal;
        this.emalHed = emalHed;
        this.memGen = memGen;
        this.memRole = memRole;
    }
}

