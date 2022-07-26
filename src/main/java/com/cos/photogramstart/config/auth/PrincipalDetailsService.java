package com.cos.photogramstart.config.auth;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username).orElseThrow(new Supplier<UsernameNotFoundException>() {
            @Override
            public UsernameNotFoundException get() {
                return new UsernameNotFoundException("해당 유저 아이디는 존재하지 않습니다. : " + username);
            }
        });

        return new PrincipalDetails(userEntity);
    }
}
