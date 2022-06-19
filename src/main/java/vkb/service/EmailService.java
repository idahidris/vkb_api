package vkb.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vkb.entity.Subscription;
import vkb.entity.UserAccount;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

@Slf4j
@Service
public class EmailService {

    private final TemplateEngine templateEngine;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public String sendMail(UserAccount user, String type, Subscription subscription, String title) {

        try {
            Context context = new Context();
            context.setVariable("user", user);
            context.setVariable("subscription", subscription);
            context.setVariable("baseUrl", getCurrentBaseUrl());
            context.setVariable("completionDate", new SimpleDateFormat("dd-MM-yyyy").format(subscription.getExpectedDeliveryDate()));
            context.setVariable("outstandingBalance", String.format("%.2f",subscription.getPrice() - subscription.getPaidAmount()));

            String process = templateEngine.process(type, context);
            javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setSubject(title);
            helper.setText(process, true);
            helper.setTo(user.getEmail());
            helper.setFrom(username);
            javaMailSender.send(mimeMessage);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return "Sent";
    }


    private static String getCurrentBaseUrl() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
    }

}