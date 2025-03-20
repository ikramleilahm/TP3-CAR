package com.example.demo.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.springframework.stereotype.Service;

@Service
public class AkkaService {
    private ActorSystem system;
    private ActorRef[] mappers;
    private ActorRef[] reducers;

    public void initActors() {
        system = ActorSystem.create("MapReduceSystem");

        reducers = new ActorRef[2];
        for (int i = 0; i < reducers.length; i++) {
            reducers[i] = system.actorOf(Props.create(ReducerActor.class), "reducer-" + i);
        }

        mappers = new ActorRef[3];
        for (int i = 0; i < mappers.length; i++) {
            mappers[i] = system.actorOf(Props.create(MapperActor.class, (Object) reducers), "mapper-" + i);
        }

        System.out.println(" AkkaService : 3 Mappers et 2 Reducers créés !");
    }
}
