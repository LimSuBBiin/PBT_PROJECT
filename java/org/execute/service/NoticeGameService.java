package org.execute.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.execute.domain.*;
import org.execute.dto.notice.CreateNoticeDto;
import org.execute.dto.notice.NoticeListDto;
import org.execute.dto.notice.NoticeRequestListDto;
import org.execute.repository.MemRepository;
import org.execute.repository.NoticeRepository;
import org.execute.repository.TeamMemberRepository;
import org.execute.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class NoticeGameService {
    private final NoticeRepository noticeRepository;
    private final MemRepository memRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    @Transactional
    public NoticeGame createNotice(CreateNoticeDto createNoticeDto){
            Optional<Mem> existingMem = memRepository.findByMemMstIdx(createNoticeDto.getMemMstIdx());
            if(existingMem.isEmpty()){
                throw new RuntimeException("유저를 찾을 수 없습니다.");
            }

            Optional<Team> existingTeam = teamRepository.findByTeamIdxOpt(createNoticeDto.getTeamIdx());
            if(existingTeam.isEmpty()){
                throw new RuntimeException("유저가 소속한 팀이 아닙니다.");
            }

            Optional<NoticeGame> existingNotice = noticeRepository.findByTitle(createNoticeDto.getNoticeTitle());
            if(existingNotice.isPresent()){
                throw new RuntimeException("이미 존재하는 글입니다.");
            }

            NoticeGame noticeGame = NoticeGame.builder()
                    .mem(existingMem.get())
                    .team(existingTeam.get())
                    .noticeTitle(createNoticeDto.getNoticeTitle())
                    .noticeDesc(createNoticeDto.getNoticeDesc())
                    .noticeLocation(createNoticeDto.getNoticeLocation())
                    .noticeGameDate(createNoticeDto.getNoticeGameDate())
                    .guardCount(createNoticeDto.getGuardCount())
                    .forwardCount(createNoticeDto.getForwardCount())
                    .centerCount(createNoticeDto.getCenterCount())
                    .cost(createNoticeDto.getCost())
                    .noticeDelYn(DelYn.N)
                    .gameStatus(GameStatus.RECRUITING)
                    .hit(0L)
                    .build();


            noticeGame.setMemNme(existingMem.get().getMemNme());
            noticeRepository.save(noticeGame);

        return noticeGame;
    }


    @Transactional
    public List<NoticeListDto> getNoticeList(Long teamIdx, Long teamRequestIdx) {
        Optional<Team> existTeam = teamRepository.findByTeamIdxOpt(teamIdx);
        Optional<TeamMember> existTm = teamMemberRepository.findByTmReqIdx(teamRequestIdx);
        if(existTeam.isEmpty()){
            throw new RuntimeException("소속중인 팀이 없습니다.");
        }

        if(existTm.isEmpty()) {
            throw new RuntimeException("소속중인 팀 멤버가 아닙니다.");
        }

        List<NoticeListDto> test = noticeRepository.getNoticeList(teamIdx, teamRequestIdx);
        if(test.isEmpty() || test == null) {
            System.out.println("test가 널입니다.");
        }

        return noticeRepository.getNoticeList(teamIdx, teamRequestIdx);
    }

    @Transactional
    public List<NoticeListDto> getMyNoticeList(Long teamIdx, Long teamRequestIdx, Long memMstIdx) {
        Optional<Team> existTeam = teamRepository.findByTeamIdxOpt(teamIdx);
        Optional<TeamMember> existTm = teamMemberRepository.findByTmReqIdx(teamRequestIdx);
        Optional<Mem> existMem = memRepository.findByMemMstIdx(memMstIdx);

        if(existTeam.isEmpty()){
            throw new RuntimeException("소속중인 팀이 없습니다.");
        }

        if(existTm.isEmpty()) {
            throw new RuntimeException("소속중인 팀 멤버가 아닙니다.");
        }

        if(existMem.isEmpty()) {
            throw new RuntimeException("로그인중인 회원이 아닙니다.");
        }

        List<NoticeListDto> test = noticeRepository.getMyNoticeList(teamIdx, teamRequestIdx, memMstIdx);
        if(test.isEmpty() || test == null) {
            System.out.println("test가 널입니다.");
        }

        return noticeRepository.getMyNoticeList(teamIdx, teamRequestIdx, memMstIdx);


    }

    @Transactional
    public List<NoticeRequestListDto> geyMyGameReqNotice(Long memMstIdx) {
        Optional<Mem> existMem = memRepository.findByMemMstIdx(memMstIdx);

        if(existMem.isEmpty()) {
            throw new RuntimeException("로그인중인 회원이 아닙니다.");
        }

        List<NoticeRequestListDto> noticeListDtoList = noticeRepository.getMyReqGameNotice(memMstIdx);
        if(noticeListDtoList.isEmpty()) {
            System.out.println("신청중인 경기가 없습니다.");
        }
        return noticeListDtoList;
    }


    @Transactional
    public Optional<Long> findIdxByMemAndTeam(Long teamIdx, Long memMstIdx) {
        System.out.println("Serviec에서 teamIdx와 memMstIdx값 확인"+teamIdx + memMstIdx);
        Optional<Long> teamMemberIdx = teamMemberRepository.findByMemAndTeam(teamIdx, memMstIdx);
        if (teamMemberIdx.isEmpty()) {
            throw new RuntimeException("팀이나 회원이 유효하지 않습니다.");
        }
        return teamMemberIdx;
    }

    @Transactional
    public void hitImpl(Long noticeGameIdx){
       noticeRepository.noticeHit(noticeGameIdx);
    }
}
