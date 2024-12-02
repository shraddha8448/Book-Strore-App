package com.user.user_service.usermicro.security;

import com.user.user_service.usermicro.modal.User;
import com.user.user_service.usermicro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public  CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User getUserByEmail = userRepository.findByEmail(email);

        if(getUserByEmail==null) {

            throw new UsernameNotFoundException("EMAIL NOT FOUND!!");
        }

        return  new CustomUserDetails(getUserByEmail);
    }

}
