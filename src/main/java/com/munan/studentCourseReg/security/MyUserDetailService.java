package com.munan.studentCourseReg.security;

import com.munan.studentCourseReg.model.AppUser;
import com.munan.studentCourseReg.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AppUser> user = userRepository.findByUsername(username);

        user.orElseThrow(()-> new UsernameNotFoundException("User "+username+" not found"));

        return user.map(MyUserDetails::new).get();
    }
}
