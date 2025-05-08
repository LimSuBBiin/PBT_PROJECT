package org.execute.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "game_request_tbl")
public class GameRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_request_idx")
    private Long gameRequestIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_game_seq")
    private NoticeGame noticeGame;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_request_idx", nullable = false)
    private TeamMember teamMember;


    @Enumerated(EnumType.STRING)
    @Column(name = "game_request_status")
    private GameRequestStatus gameRequestStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;


    public static GameRequest createGameRequest(NoticeGame noticeGame, TeamMember teamMember, GameRequestStatus gameRequestStatus, Position position){
        GameRequest gameRequest = new GameRequest();
        gameRequest.setNoticeGame(noticeGame);
        gameRequest.setTeamMember(teamMember);
        gameRequest.setGameRequestStatus(GameRequestStatus.신청);
        gameRequest.setPosition(position);
        return gameRequest;
    }

    public void cancel() {
        this.gameRequestStatus = GameRequestStatus.취소;
    }

    public void reJoin() {
        this.gameRequestStatus = GameRequestStatus.신청;

    }
}
