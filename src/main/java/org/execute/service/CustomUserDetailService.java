package org.execute.service;

import org.execute.domain.Mem;
import org.execute.repository.MemRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final MemRepository memRepository;

    public CustomUserDetailService(MemRepository memRepository) {
        this.memRepository = memRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Mem mem = memRepository.findByMemActNotOpt(username);
        if (mem == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return User.builder()
                .username(mem.getMemAct())
                .password(mem.getMemPwd())
                .roles(mem.getRoles()) // 권한을 배열로 반환
                .build();
    }
}
