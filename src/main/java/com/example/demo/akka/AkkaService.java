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

    // Méthode pour initialiser l'architecture Akka
    public void initActors() {
        // Création du système d'acteurs
        system = ActorSystem.create("MapReduceSystem");

        // Création des 2 ReducerActor
        reducers = new ActorRef[2];
        for (int i = 0; i < reducers.length; i++) {
            reducers[i] = system.actorOf(Props.create(ReducerActor.class), "reducer-" + i);
        }

        // Création des 3 MapperActor en leur passant les Reducers
        mappers = new ActorRef[3];
        for (int i = 0; i < mappers.length; i++) {
            mappers[i] = system.actorOf(Props.create(MapperActor.class, (Object) reducers), "mapper-" + i);
        }

        System.out.println(" AkkaService : 3 Mappers et 2 Reducers créés !");
    }
}
