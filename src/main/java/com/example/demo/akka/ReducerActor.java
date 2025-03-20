package com.example.demo.akka;

import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.Map;

public class ReducerActor extends UntypedActor {
    private final Map<String, Integer> wordCountMap = new HashMap<>();

    @Override
    public void onReceive(Object message) {
        if (message instanceof String) {
            String word = (String) message;
            System.out.println("Reducer [" + getSelf().path().name() + "] a reçu : " + word);
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            System.out.println("Reducer [" + getSelf().path().name() + "] a compté : " + word);
        } else if (message instanceof GetWordCountMap) {
            getSender().tell(wordCountMap, getSelf());
        } else {
            unhandled(message);
        }
    }
}
