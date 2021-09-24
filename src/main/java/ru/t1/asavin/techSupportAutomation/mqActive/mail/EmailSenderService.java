package ru.t1.asavin.techSupportAutomation.mqActive.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Value("${app.email.from}")
    private final String emailFrom;

    @Value("${app.email.subject}")
    private final String emailSubject;

    public EmailSenderService(JavaMailSender javaMailSender,
                        @Value("${app.email.from}") String emailFrom,
                        @Value("${app.email.subject}") String emailSubject) {
        this.javaMailSender = javaMailSender;
        this.emailFrom = emailFrom;
        this.emailSubject = emailSubject;
    }

    public void sendEmail(String[] list){
        String incidentId = list[0];
        String assignedAnalystUsername = list[1];
        String assignedAnalystEmail = list[2];
        String emailText = "Hello, " + assignedAnalystUsername + ". You were assigned to solve incident with the id " + incidentId;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailFrom);
        msg.setTo(assignedAnalystEmail);
        msg.setSubject(emailSubject);
        msg.setText(emailText);

        javaMailSender.send(msg);
        log.info("Assignment message sent to the email " + assignedAnalystEmail);
    }
}
