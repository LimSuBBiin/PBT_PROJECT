package org.execute.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.execute.domain.Mem;
import org.execute.dto.MemDto;
import org.execute.repository.MemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemRepository memRepository;

//    public MyPageService(MemRepository memRepository) {
//        this.memRepository = memRepository;
//    }


    @Transactional
    //수정
    public void updateMem(Long memMstIdx, String memNme, String phonHed, String phonBod, String phonTal, String emalTal, String emalHed){
        System.out.println("전달된 memMstIdx: " + memMstIdx);
        Optional<Mem> mem = memRepository.findByMemMstIdx(memMstIdx);
        System.out.println("findByMemMstIdx 호출 후 mem 존재 여부: " + mem.isPresent());
        if(mem.isPresent()) {
            memRepository.updateMyPage(memMstIdx, memNme, phonHed, phonBod, phonTal, emalTal, emalHed);
            System.out.println("수정완료");
        } else {
            System.out.println("수정실패 : 사용자를 찾을 수 없습니다.");
        }

    }

    @Transactional
    public Mem findByMemInfoSeq(Long memSeq){
       return memRepository.findByMemInfoSeq(memSeq);
    }

    @Transactional
    public MemDto findMemProfile(Long memMstIdx){
        return memRepository.findMemProfile(memMstIdx);
    }

    @Transactional
    public Optional<Mem> findByMemMstIdx(Long memMstIdx){
        return memRepository.findByMemMstIdx(memMstIdx);
    }
}
