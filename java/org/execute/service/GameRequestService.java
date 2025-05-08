package org.execute.service;

import com.mysql.cj.protocol.x.Notice;
import lombok.RequiredArgsConstructor;
import org.execute.domain.*;
import org.execute.dto.GameRequest.GameRequestDto;
import org.execute.repository.GameRequestRepository;
import org.execute.repository.NoticeRepository;
import org.execute.repository.TeamMemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameRequestService {
    private final TeamMemberRepository teamMemberRepository;
    private final NoticeRepository noticeRepository;
    private final GameRequestRepository gameRequestRepository;


    public void joinRequest(GameRequestDto gameRequestDto) {
        Optional<TeamMember> teamMember = teamMemberRepository.findByTmReqIdx(gameRequestDto.getTeamRequestIdx());
        Optional<NoticeGame> noticeGame = noticeRepository.findByidx(gameRequestDto.getNoticeGameIdx());
        Optional<GameRequest> gameRequestOpt = gameRequestRepository.findExistJoin(gameRequestDto.getNoticeGameIdx(), gameRequestDto.getTeamRequestIdx());

        if (teamMember.isEmpty() || noticeGame.isEmpty()) {
            throw new RuntimeException("해당 글이 없거나, 팀 멤버가 아닙니다.");
        }

        if (gameRequestOpt.isPresent()) {
            GameRequest gameRequest = gameRequestOpt.get();

            if (gameRequest.getGameRequestStatus() == GameRequestStatus.신청) {
                throw new RuntimeException("이미 신청한 회원입니다.");
            } else if (gameRequest.getGameRequestStatus() == GameRequestStatus.취소) {
                // 재신청
                gameRequestRepository.reJoinGameRequest(gameRequestDto.getNoticeGameIdx(), gameRequestDto.getTeamRequestIdx(), gameRequestDto.getPosition());
                return;
            }
        }

        // 처음 신청
        Mem mem = teamMember.get().getMember();
        GameRequest gameRequest = GameRequest.createGameRequest(
                noticeGame.get(),
                teamMember.get(),
                GameRequestStatus.신청,
                gameRequestDto.getPosition()
        );

        gameRequestRepository.save(gameRequest);
    }

    public void cacelReq(Long noticeGameIdx, Long teamRequestIdx){
        Optional<NoticeGame> existnotice = noticeRepository.findByidx(noticeGameIdx);
        if(existnotice.isEmpty()){
            throw new RuntimeException("해당글은 존재하지 않습니다.");
        }

        Optional<TeamMember> existTm = teamMemberRepository.findByTmReqIdx(teamRequestIdx);
        if(existTm.isEmpty()){
            throw new RuntimeException("해당 tm이 아닙니다.");
        }
        gameRequestRepository.cancelGameRequest(noticeGameIdx,teamRequestIdx);
        System.out.println("service에서 gameRequest 거절");

    }

    public void reJoinReq(Long noticeGameIdx, Long teamRequestIdx, Position position){
        Optional<NoticeGame> existnotice = noticeRepository.findByidx(noticeGameIdx);
        if(existnotice.isEmpty()){
            throw new RuntimeException("해당글은 존재하지 않습니다.");
        }

        Optional<TeamMember> existTm = teamMemberRepository.findByTmReqIdx(teamRequestIdx);
        if(existTm.isEmpty()){
            throw new RuntimeException("해당 tm이 아닙니다.");
        }

        gameRequestRepository.reJoinGameRequest(noticeGameIdx,teamRequestIdx, position);
        System.out.println("service에서 재신청 완료");
    }
}
