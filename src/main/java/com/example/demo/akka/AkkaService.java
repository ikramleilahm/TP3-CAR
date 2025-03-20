package com.example.demo.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.springframework.stereotype.Service;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AkkaService {
    private ActorSystem system;
    private ActorRef[] mappers;
    private ActorRef[] reducers;
    private final Map<String, Integer> wordCounts = new HashMap<>();


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

    public void sendFileToMappers(String fileContent) {
        String[] lines = fileContent.split("\n");
        for (int i = 0; i < lines.length; i++) {
            mappers[i % mappers.length].tell(lines[i], ActorRef.noSender());
        }
    }

    public Map<String, Integer> getAggregatedWordCounts() {
        Map<String, Integer> aggregatedCounts = new HashMap<>();
        Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

        for (ActorRef reducer : reducers) {
            try {
                Future<Object> future = Patterns.ask(reducer, new GetWordCountMap(), timeout);
                Map<String, Integer> reducerMap = (Map<String, Integer>) Await.result(future, Duration.create(5, TimeUnit.SECONDS));

                for (Map.Entry<String, Integer> entry : reducerMap.entrySet()) {
                    aggregatedCounts.put(entry.getKey(), aggregatedCounts.getOrDefault(entry.getKey(), 0) + entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return aggregatedCounts;
    }






}
