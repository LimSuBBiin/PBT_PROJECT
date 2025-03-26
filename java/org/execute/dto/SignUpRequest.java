package org.execute.dto;

import lombok.*;

@Getter

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {
    private String memAct;
    private String memPwd;

    public void setMemAct(String memAct) {
        this.memAct = memAct;
    }

    public void setMemPwd(String memPwd) {
        this.memPwd = memPwd;
    }
}
