package org.execute.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.TypeAlias;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notice_game_tbl")
public class NoticeGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "NOTICE_GAME_IDX")
    private Long noticeGameIdx;

    @Column(name = "NOTICE_GAME_SEQ", nullable = true)
    private Long noticeGameSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_idx")
    private Team team;


    //리스트용을 위해 조인하는게맞음.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_mst_idx")
    private Mem mem;


    @NotNull
    @Column(name = "notice_title", nullable = true)
    private String noticeTitle;



    @Column(name = "notice_desc")
    private String noticeDesc;

    @NotNull
    @Column(name = "notice_location")
    private String noticeLocation;

    @Column(name = "hit" )
    private Long hit = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "notice_del_yn")
    private DelYn noticeDelYn = DelYn.N;

    @Column(name = "notice_game_date")
    private LocalDateTime noticeGameDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "notice_game_status")
    private GameStatus gameStatus = GameStatus.RECRUITING;

    @Column(name = "cost")
    private String cost;


    //포지션별 모집 인원
    @NotNull
    @Column(name = "guard_count")
    private Long guardCount;
    @NotNull
    @Column(name = "forward_count")
    private Long forwardCount;
    @NotNull
    @Column(name = "center_count")
    private Long centerCount;


    //비정규화 데이터
    @Column(name = "memNme")
    private String memNme;


    @JsonIgnore
    @OneToMany(mappedBy = "noticeGame", fetch = FetchType.LAZY)
    List<GameRequest> gameRequests = new ArrayList<>(); // (1:N)

    public void setTeam(Team team) {this.team = team;}


    public GameStatus gameStatusNow(LocalDateTime noticeGameDate) {
        if(noticeGameDate == null) {
            System.out.println("경기시간을 설정하세요");
            return GameStatus.UNKNOWN;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime recruitingDeadline = noticeGameDate.minusHours(1);
        if(now.isBefore(recruitingDeadline)){
            return GameStatus.RECRUITING;
        } else {
            return GameStatus.CLOSED;
        }
    }
//    @PrePersist
//    @PreUpdate
//    public void gameStatusNow2() {
//        if (noticeGameDate != null){
//            LocalDateTime now = LocalDateTime.now();
//            LocalDateTime recruitingDeadline = noticeGameDate.minusHours(1);
//            if(now.isBefore(recruitingDeadline)) {
//                this.gameStatus = GameStatus.RECRUITING;
//            } else {
//                this.gameStatus = GameStatus.CLOSED;
//            }
//        } else {
//            this.gameStatus = GameStatus.UNKNOWN;
//        }
//    }

    public Long totalCount(){
        return  this.guardCount+ this.forwardCount+ this.centerCount;
    }

//    public static NoticeGame createNotice(Team team, ){
//        NoticeGame noticeGame = new NoticeGame();
//
//    }
//

}
