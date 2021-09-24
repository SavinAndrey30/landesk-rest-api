package ru.t1.asavin.techSupportAutomation.mqActive.consumer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.t1.asavin.techSupportAutomation.mqActive.MQConstants;
import ru.t1.asavin.techSupportAutomation.mqActive.mail.EmailSenderService;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class MQConsumer {

    private final EmailSenderService emailService;

    @JmsListener(destination = MQConstants.MQ_NAME)
    public void processMessage(String[] list) {
        log.debug("MQConsumer received message " + Arrays.toString(list));
        emailService.sendEmail(list);
    }

    @JmsListener(destination = MQConstants.MQ_NAME)
    public void logProcessMessage(String[] list) {
        log.info("Incident with the id " + list[0] + " is assigned to the analyst with the username " + list[1] + ". Notification is sent to the email " + list[2]);
    }
}
