package org.example.kino.Controller;

import org.example.kino.Model.Film;
import org.example.kino.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@GetMapping
public class FilmController {

    private final FilmRepository filmRepository;

    @Autowired
    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmRepository = filmRepository;
    }

    @GetMapping("/id")
    public Film hentFilm(@PathVariable int id) {
        return filmRepository.findById(id);
    }

    @PostMapping
    public int lagFilm(@RequestBody Film film) {
        return filmRepository.save(film);
    }

    @PutMapping("/id")
    public boolean oppdaterFilm(@PathVariable int id, @RequestBody Film film) {
        return filmRepository.update(film);
    }

    @DeleteMapping("/id")
    public boolean slettFilm(@PathVariable int id) {
        return filmRepository.delete(id);
    }
}
