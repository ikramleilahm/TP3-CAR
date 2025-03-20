package com.example.demo.akka;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class MapperActor extends UntypedActor {
    private final ActorRef[] reducers;

    public MapperActor(ActorRef[] reducers) {
        this.reducers = reducers;
    }
    @Override
    public void onReceive(Object message) {
        if (message instanceof String) {
            String line = (String) message;
            System.out.println("Mapper [" + getSelf().path().name() + "] a reçu : " + line);
        } else {
            unhandled(message);
        }
    }
}
