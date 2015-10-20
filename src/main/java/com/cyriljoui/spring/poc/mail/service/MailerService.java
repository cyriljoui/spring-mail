package com.cyriljoui.spring.poc.mail.service;

import com.cyriljoui.spring.poc.mail.model.User;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class MailerService {

    public static final String ENCODING = "utf-8";
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private MessageSource messageSource;

    @Value("${mail.from}")
    private String emailFrom;

    public void sendRegistrationEmail(User user) {
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
                    velocityEngine, getI18nVelocityMailTemplate("signup.vm", locale), ENCODING, model);
            message.setText(text, true);
        };
        this.mailSender.send(preparator);
    }

    public void sendPasswordRecoveryEmail(User user) {
        // Password recovery random UUID
        UUID uuid = UUID.randomUUID();

        Locale locale = user.getLocale();
        String subject = messageSource.getMessage("mail.passwordrecovery.subject", new Object[]{}, locale);
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(user.getEmail());
            message.setFrom(emailFrom);
            message.setSubject(subject);
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            model.put("resetToken", uuid.toString());
            String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, getI18nVelocityMailTemplate("password.vm", locale), ENCODING, model);
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
