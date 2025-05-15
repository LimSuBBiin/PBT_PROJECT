package org.execute.dto;

import lombok.*;

@Getter

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    private String memNme;
    private String memAct;
    private String memPwd;

    public void setMemNme(String memNme) {
        this.memNme = memNme;
    }

    public void setMemAct(String memAct) {
        this.memAct = memAct;
    }

    public void setMemPwd(String memPwd) {
        this.memPwd = memPwd;
    }
}
