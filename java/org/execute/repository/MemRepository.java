package org.execute.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.execute.domain.Mem;
import org.execute.dto.MemDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemRepository {
    @PersistenceContext
    private final EntityManager em;

//    public MemRepository(EntityManager em) {
//        this.em = em;
//    }


    @Transactional
    public void save(Mem mem) {
        System.out.println("✅ 저장 전 회원 ID(memMstIdx): " + mem.getMemMstIdx());
        em.persist(mem);
        System.out.println("✅ 회원 저장 완료: " + mem);
        em.flush();
        System.out.println("✅ 저장 완료 후 회원 ID(memMstIdx): " + mem.getMemMstIdx());
    }

    public Mem findByIDX(Long memMstIdx) {
        return em.createQuery("select m from Mem m where m.memMstIdx = :memMstIdx", Mem.class)
                .setParameter("memMstIdx", memMstIdx)
                .getSingleResult();
    }


    public Optional<Mem> findByMemAct(String memAct) {
        try {
            return Optional.ofNullable(
                    em.createQuery("select m from Mem m where m.memAct = :memAct", Mem.class)
                            .setParameter("memAct", memAct)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();  // 회원이 없는 경우 빈 값 반환
        }
    }

    public Optional<Mem> findByMemMstIdx(Long memMstIdx) {
        try {
            return Optional.ofNullable(
                    em.createQuery("select m from Mem m where m.memMstIdx = :memMstIdx",Mem.class)
                            .setParameter("memMstIdx", memMstIdx)
                            .getSingleResult()
            );
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    public Mem findByMemActNotOpt(String memAct) {
        return em.createQuery("select m from Mem m where m.memAct = :memAct", Mem.class)
                .setParameter("memAct", memAct)
                .getSingleResult();
    }




    public List<Mem> findByAllMem() {
        return em.createQuery("select m from Mem m", Mem.class)
                .getResultList();
    }

    @Transactional
    public void updateMyPage(Long memMstIdx, String memNme, String phonHed, String phonBod, String phonTal, String emalTal, String emalHed){
        Mem mem = em.find(Mem.class, memMstIdx);
        if(mem == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        mem.setMemNme(memNme);
        mem.setPhonHed(phonHed);
        mem.setPhonBod(phonBod);
        mem.setPhonTal(phonTal);
        mem.setEmalHed(emalHed);
        mem.setEmalTal(emalTal);

        em.merge(mem);
        System.out.println("사용자 정보가 성공적으로 수정되었습니다.");
        }

//    @Transactional
//    public void updateMyPage(Long memMstIdx, String memNme, String phonHed, String phonBod, String phonTal, String emalTal, String emalHed){
//        String jpql = "UPDATE MEM m SET m.memNme = :memNme, m.phonHed = :phonHed, m.phonBod = :phonBod, m.phonTal = :phonTal, m.emalTal = :emalTal, m.emalHed = :emalHed WHERE m.memMstIdx = :memMstIdx";
//        int updatedCount = em.createQuery(jpql)
//                .setParameter("memNme", memNme)
//                .setParameter("phonHed", phonHed)
//                .setParameter("phonBod",phonBod)
//                .setParameter("phonTal", phonTal)
//                .setParameter("emalHed", emalHed)
//                .setParameter("emalTal", emalTal)
//                .setParameter("memMstIdx", memMstIdx)
//                .executeUpdate();
//
//
//        if (updatedCount > 0) {
//            System.out.println("사용자 정보가 성공적으로 수정되었습니다.");
//        } else {
//            System.out.println("수정 실패: 사용자를 찾을 수 없습니다.");
//        }
//    }


    @Transactional
    public Mem findByMemInfoSeq(Long memSeq) {
        return em.createQuery(
                        "SELECT m.memAct, m.memPwd, m.memNme, m.phonHed, m.phonBod, m.phonTal FROM Mem m WHERE m.memSeq = :memSeq", Mem.class)
                .setParameter("memSeq", memSeq)
                .getSingleResult();
    }

    @Transactional
    public MemDto findMemProfile(Long memMstIdx) {
        try {
            return em.createQuery(
                            "SELECT new org.execute.dto.MemDto(m.memMstIdx, m.memSeq, m.memAct, m.memPwd, m.memNme, m.phonHed, m.phonBod, m.phonTal, m.emalTal, m.emalHed, m.memGen, m.memRole) " +
                                    "FROM Mem m " +
                                    "WHERE m.memMstIdx = :memMstIdx", MemDto.class)
                    .setParameter("memMstIdx", memMstIdx)
                    .getSingleResult();
        }catch (NoResultException e) {
            throw new EntityNotFoundException("해당정보를 찾을 수 없습니다.");
        }
    }
//    @Transactional
//    public List<MemDto> findByMemInfoAct2(String memAct, String memPwd, String memNme, String phonHed, String phonBod, String phonTal) {
//        return em.createQuery("SELECT m.memAct, m.memPwd, m.memNme, m.phonHed, m.phonBod, m.phonTal FROM MEM m WHERE m.memAct = :memAct", MemDto.class)
//                .getResultList();
//
//    }
//


}
