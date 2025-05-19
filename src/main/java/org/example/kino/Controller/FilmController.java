package org.example.kino.Controller;

import org.example.kino.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@GetMapping
public class FilmController {

    private final FilmRepository filmRepository;

    @Autowired

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }




}
