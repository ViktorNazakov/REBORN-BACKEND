package com.example.Boot.Service;

import com.example.Boot.User.User;
import com.example.Boot.Repository.UserRepo;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class UserService implements UserServiceInterface {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender javaMailSender;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void deleteById(int id) {
        userRepo.deleteById(id);
    }

    @Override
    public User saveUser(User user) throws MessagingException, UnsupportedEncodingException {

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        
        sendVerificationEmail(user);
        
        return userRepo.save(user);
    }

    private void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "Please verify your registration";
        String senderName = "Reborn Team";
        String mailContent = "<p>Dear" + user.getUsername() + " </p>";
        mailContent += "<p> Please click the link below to verify your registration:</p>";

        String verifyURL = "http://localhost:8086/verify?code=" + user.getVerificationCode();
        mailContent += "<h3><a href ="+ verifyURL + ">VERIFY</a></h3>";
        mailContent += "<p>Thank you<br>The Reborn Team</p>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("reborn.service@gmail.com",senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);

        javaMailSender.send(message);

    }

    public boolean verify(String verificationCode) {
        User user = userRepo.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setEnabled(true);
            userRepo.save(user);
            return true;
        }
    }

    @Override
    public User findUserById(int userID) {
        Optional<User> user = userRepo.findById(userID);
        return user.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = userRepo.findByEmail(email);

        if (user.isEmpty() || !user.get().isEnabled()) {
            throw new UsernameNotFoundException("Invalid email or password");

        }

        return user.get();

    }


}
