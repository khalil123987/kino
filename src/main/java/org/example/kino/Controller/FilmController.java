package org.example.kino.Controller;

import org.example.kino.Model.Film;
import org.example.kino.Repository.FilmRepository;
import org.example.kino.Service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/filmer")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> hentAlle() {
        return filmService.hentAlleFilmer();
    }

    @GetMapping("/{id}")
    public Film hentEn(@PathVariable int id) {
        return filmService.hentFilmVedId(id);
    }

    @PostMapping
    public int lagFilm(@RequestBody Film film) {
        return filmService.lagNyFilm(film).getFilmnr();
    }

    @PutMapping("/{id}")
    public boolean oppdater(@PathVariable int id, @RequestBody Film film) {
        film.setFilmnr(id);
        return filmService.oppdaterFilm(film);
    }

    @DeleteMapping("/{id}")
    public boolean slett(@PathVariable int id) {
        return filmService.slettFilm(id);
    }
}
