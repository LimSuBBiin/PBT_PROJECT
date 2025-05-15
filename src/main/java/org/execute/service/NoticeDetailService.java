package org.execute.service;

import lombok.RequiredArgsConstructor;
import org.execute.domain.Mem;
import org.execute.domain.NoticeGame;
import org.execute.dto.notice.GameReqFoamDto;
import org.execute.dto.notice.NoticeDetailDto;
import org.execute.repository.MemRepository;
import org.execute.repository.NoticeDetailRepository;
import org.execute.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class NoticeDetailService {
    private final NoticeDetailRepository noticeDetailRepository;
    private final NoticeRepository noticeRepository;
    private final MemRepository memRepository;
    public NoticeDetailDto getNoticeDetail (Long noticeGameIdx){
        System.out.println("Service에서 noticeGameIdx값 확인:"+noticeGameIdx);
        Optional<NoticeGame> noticeDetailDto = noticeRepository.findByidx(noticeGameIdx);
        if(noticeDetailDto.isEmpty()){
            throw new RuntimeException("해당글이 존재하지 않습니다.");
        }

        return noticeDetailRepository.noticeDetail(noticeGameIdx);
    }

    public GameReqFoamDto getReqFoam (Long memMstIdx) {
        System.out.println("Service에서 memMstIdx값 확인: " +memMstIdx);
        Optional<Mem> memOptional = memRepository.findByMemMstIdx(memMstIdx);
        if(memOptional.isEmpty()) {
            throw new RuntimeException("Service에서 해당 회원 오류");
        }

        return noticeDetailRepository.gameReqFoam(memMstIdx);
    }
}
