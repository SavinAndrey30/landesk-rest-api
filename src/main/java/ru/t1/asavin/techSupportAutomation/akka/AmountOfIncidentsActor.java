package ru.t1.asavin.techSupportAutomation.akka;

import akka.actor.UntypedAbstractActor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.t1.asavin.techSupportAutomation.dto.IncidentDto;
import ru.t1.asavin.techSupportAutomation.service.IncidentService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AmountOfIncidentsActor extends UntypedAbstractActor {

    private final IncidentService incidentService;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.debug("AmountOfIncidentsActor started");
    }

    @Override
    public void postStop() throws Exception {
        log.debug("AmountOfIncidentsActor is finishing");
        super.postStop();
    }

    @Override
    public void onReceive(Object message) {
        log.debug("onReceive method. Message is: " + message);
        if (message.equals("amountOfIncidents")) {
            List<IncidentDto> incidentDtos = incidentService.findAll();
            log.info(incidentDtos.size() + " incidents are registered in the app");
        } else {
            log.error("Can't handle message received by the AmountOfIncidentsActor");
            unhandled(message);
        }
    }
}
