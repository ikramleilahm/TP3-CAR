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

            String[] words = line.split("\\s+");

            for (String word : words) {
                ActorRef chosenReducer = partition(reducers, word);
                chosenReducer.tell(word, getSelf());
                System.out.println("Mapper [" + getSelf().path().name() + "] envoie : " + word + " à Reducer");
            }
        } else {
            unhandled(message);
        }
    }

    private ActorRef partition(ActorRef[] reducers, String word) {
        int hash = word.hashCode();
        int index = Math.abs(hash) % reducers.length;
        return reducers[index];
    }


}
