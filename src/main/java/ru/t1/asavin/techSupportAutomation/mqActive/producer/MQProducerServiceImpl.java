package ru.t1.asavin.techSupportAutomation.mqActive.producer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.t1.asavin.techSupportAutomation.mqActive.MQConstants;

import java.util.Arrays;


@Service
@RequiredArgsConstructor
@Slf4j
public class MQProducerServiceImpl implements MQProducerService {

    private final JmsTemplate jmsTemplate;

    public void sendMessage(String[] list) {
        log.debug("MQProducerService: generated message is " + Arrays.toString(list));
        jmsTemplate.convertAndSend(MQConstants.MQ_NAME, list);
    }
}
