package com.example.demo.akka;

import akka.actor.UntypedActor;

public class ReducerActor extends UntypedActor {
    @Override
    public void onReceive(Object message) {
        if (message instanceof String) {
            String word = (String) message;
            System.out.println("Reducer [" + getSelf().path().name() + "] a reçu : " + word);
        } else {
            unhandled(message);
        }
    }

}
