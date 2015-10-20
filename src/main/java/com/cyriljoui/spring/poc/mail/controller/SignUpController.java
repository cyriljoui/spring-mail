package com.cyriljoui.spring.poc.mail.controller;

import com.cyriljoui.spring.poc.mail.model.User;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class SignUpController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private MessageSource messageSource;

    @Value("${mail.from}")
    private String emailFrom;

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
        sendEmail(user);

        return "signup";
    }

    private void sendEmail(User user) {
        Locale locale = user.getLocale();
        String subject = messageSource.getMessage("mail.registration.subject", new Object[] {user.getFirstname()}, locale);
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(user.getEmail());
            message.setFrom(emailFrom);
            message.setSubject(subject);
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, getI18nVelocityMailTemplate("signup.vm", locale), "utf-8", model);
            message.setText(text, true);
        };
        this.mailSender.send(preparator);
    }

    private String getI18nVelocityMailTemplate(String mailFileTemplate, Locale locale) {
        String language = locale.getLanguage();
        String templateFolder = "/templates/mail/";
        String templateFile = templateFolder + language + "/" + mailFileTemplate;

        // Try template with locale suffix
        if (getClass().getResource(templateFile) == null) {
            // Default file has no suffix
            templateFile = templateFolder + "en/" + mailFileTemplate;
        }
        return templateFile;
    }
}
