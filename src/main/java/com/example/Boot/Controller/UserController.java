package com.example.Boot.Controller;

import com.example.Boot.User.User;
import com.example.Boot.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserService userService;

    @GetMapping({"/home", "/"})
    public String HomePage() {
        return "index";
    }

    @GetMapping("/about")
    public String AboutPage() {
        return "about";
    }

    @GetMapping("/contacts")
    public String ContactsPage() {
        return "contacts";
    }
    @GetMapping("/register")
    public ModelAndView RegisterPage() {
        ModelAndView mav = new ModelAndView("register");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }

    @GetMapping("/login")
    public String LoginPage() {
        return "login";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user) throws MessagingException, UnsupportedEncodingException {
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        User userDetails = new User(user.getUsername(), user.getEmail(), hashedPassword);
        userService.saveUser(userDetails);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String ProfilePage() {
        return "profile";
    }

    @GetMapping("/logout")
    public String LogOutUser() {
        return "redirect:/contacts";
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code) {
        boolean verified = userService.verify(code);
        return verified ? "redirect:/register-success" : "redirect:/register-fail";
    }
    @GetMapping("/register-success")
    public String registerSuccess() {
        return "registerSuccess";
    }

    @GetMapping("/register-fail")
    public String registerFail() {
        return "registerFail";
    }

}
