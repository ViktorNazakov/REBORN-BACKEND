package com.example.Boot.Service;

import com.example.Boot.User.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Component
public interface UserServiceInterface extends UserDetailsService {
    User saveUser(User user) throws MessagingException, UnsupportedEncodingException;

    User findUserById(int userID);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
