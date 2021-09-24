package ru.t1.asavin.techSupportAutomation.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Log
public class ActorScheduler {

    private final long DELAY = 10 * 60 * 1000;
    private final ApplicationContext context;
    private ActorRef amountOfIncidentsActor;

    @Scheduled(fixedDelay = DELAY)
    public void performRegularAction() {
        amountOfIncidentsActor.tell("amountOfIncidents", ActorRef.noSender());
    }

    @PostConstruct
    public void postConstructMethod() {
        ActorSystem system = context.getBean(ActorSystem.class);
        SpringExtension springExtension = context.getBean(SpringExtension.class);
        amountOfIncidentsActor = system.actorOf(springExtension.props("amountOfIncidentsActor"));
    }
}
