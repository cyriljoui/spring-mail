package com.cyriljoui.spring.poc.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
public class SignUpController {

    @Autowired
    private MailSender mailSender;

    @RequestMapping("/signup")
    public String signUp(Locale locale, Model model) {
        // Get local from thread local (Spring context handler)
        // Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("locale", locale);

        sendEmail(locale);

        return "signup";
    }

    private void sendEmail(Locale locale) {
        SimpleMailMessage message = new SimpleMailMessage();
        String from = "poc-springmail@clip2.pro";
        String to = "cyril.joui@gmail.com";
        String subject = "Subject Spring POC test java mail - Locale: " + locale;
        String body = "Body mail ..." + locale;
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
