package com.example.demo.akka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/akka")
public class AkkaController {
    @Autowired
    private AkkaService akkaService;
    @PostMapping("/init")
    public String initAkka() {
        akkaService.initActors();
        return " Acteurs Akka initialisés avec succès !";
    }

}
