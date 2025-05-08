package org.execute.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mem")
public class Mem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 적용
    @Column(name = "MEM_MST_IDX")
    private Long memMstIdx;

    @Column(name = "MEM_SEQ", nullable = true)
    private Long memSeq;

    @Column(name = "MEM_ACT", nullable = false, length = 30)
    private String memAct;

    @Column(name = "MEM_PWD", nullable = false, length = 200)
    private String memPwd;

    @Column(name = "MEM_NME", length = 30)
    private String memNme;

    @Column(name = "PHON_HED", length = 3)
    private String phonHed;

    @Column(name = "PHON_BOD", length = 4)
    private String phonBod;

    @Column(name = "PHON_TAL", length = 4)
    private String phonTal;

    @Column(name = "EMAL_HED", length = 50)
    private String emalHed;

    @Column(name = "EMAL_TAL", length = 50)
    private String emalTal;

    @Enumerated(EnumType.STRING)
    @Column(name = "MEM_GEN", length = 3)
    private Gen memGen;


    //관리자 확인용
    @Column(name = "MEM_ROLE", length = 20)
    private String memRole;
    
    
    //회원 키
    @Column(name = "MEM_HEIGHT", length = 10)
    private String memHeight;

    //주 포지션
    @Enumerated(EnumType.STRING)
    @Column(name = "MAIN_PST", length = 10)
    private Position mainPst;
    
    //서브 포지션
    @Enumerated(EnumType.STRING)
    @Column(name = "SUB_PST", length = 10)
    private Position subPst;
    
    //회원 몸무게
    @Column(name = "MEM_WEIGHT", length = 10)
    private String memWeight;

    @Column(name = "MEM_HIST", length = 10)
    private String memHist;

    public String[] getRoles() {
        return new String[]{this.memRole}; // memRole을 배열로 변환
    }

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TeamMember> teamMembers = new ArrayList<>(); // 팀원 관계 (1:N)

}
