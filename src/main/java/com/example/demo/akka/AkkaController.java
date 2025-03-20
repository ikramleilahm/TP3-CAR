package com.example.demo.akka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/akka")
public class AkkaController {
    @Autowired
    private AkkaService akkaService;

    @GetMapping("/home")
    public ModelAndView showHome() {
        return new ModelAndView("akka/home");
    }

    @PostMapping("/init")
    public ModelAndView initAkka() {
        akkaService.initActors();
        ModelAndView mav = new ModelAndView("akka/home");
        mav.addObject("message", " Acteurs Akka initialisés avec succès !");
        return mav;
    }

}
