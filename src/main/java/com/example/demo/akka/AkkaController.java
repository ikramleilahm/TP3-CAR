package com.example.demo.akka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

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
        mav.addObject("message", "Acteurs Akka initialisés avec succès !");
        return mav;
    }

    @PostMapping("/upload")
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file) {
        ModelAndView mav = new ModelAndView("akka/home");

        if (!file.isEmpty()) {
            try {
                // Lire le contenu du fichier
                StringBuilder content = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }

                akkaService.sendFileToMappers(content.toString());
                mav.addObject("message", "Fichier analysé avec succès !");
            } catch (Exception e) {
                mav.addObject("message", "Erreur lors de l'analyse du fichier : " + e.getMessage());
            }
        } else {
            mav.addObject("message", "Veuillez sélectionner un fichier.");
        }

        return mav;
    }

    @GetMapping("/getWordCounts")
    public ModelAndView getWordCounts() {
        Map<String, Integer> wordCounts = akkaService.getAggregatedWordCounts();
        ModelAndView mav = new ModelAndView("akka/home");
        mav.addObject("wordCounts", wordCounts);
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView searchWord(@RequestParam("word") String word) {
        ModelAndView mav = new ModelAndView("akka/home");

        Map<String, Integer> wordCounts = akkaService.getAggregatedWordCounts();
        Integer count = wordCounts.getOrDefault(word, 0);

        mav.addObject("message", "Le mot '" + word + "' apparaît " + count + " fois.");
        mav.addObject("wordCounts", wordCounts);

        return mav;
    }
}
