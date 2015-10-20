package com.cyriljoui.spring.poc.mail.controller;

import com.cyriljoui.spring.poc.mail.model.User;
import com.cyriljoui.spring.poc.mail.service.MailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
public class SignUpController {

    @Autowired
    private MailerService mailerService;

    @RequestMapping("/signup")
    public String signUp(Locale locale, Model model) {
        // Get local from thread local (Spring context handler)
        // Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("locale", locale);

        User user = new User();
        user.setEmail("cyril.joui@gmail.com");
        user.setFirstname("Cyril");
        user.setLastname("Joui");
        user.setLocale(locale);
        mailerService.sendRegistrationEmail(user);

        return "signup";
    }

    @RequestMapping("/password")
    public String password(Locale locale, Model model) {
        // Get local from thread local (Spring context handler)
        // Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("locale", locale);

        User user = new User();
        user.setEmail("cyril.joui@gmail.com");
        user.setFirstname("Cyril");
        user.setLastname("Joui");
        user.setLocale(locale);
        mailerService.sendPasswordRecoveryEmail(user);

        return "password";
    }
}
