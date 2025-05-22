// implementert og testet av Sahil
package org.example.kino.Controller;

import org.example.kino.Model.Film;
import org.example.kino.Service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Controller klassen håndterer HTTP forespørsler
@RestController
@RequestMapping("/api/filmer")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    // Her henter og returnerer programme en liste over alle filmer
    @GetMapping
    public List<Film> hentAlle() {
        return filmService.hentAlleFilmer();
    }

    // Her henter programme kun 1 film basert på ID
    @GetMapping("/{id}")
    public Film hentEn(@PathVariable int id) {
        Optional<Film> film = filmService.hentFilmVedId(id);
        return film.orElse(null);
    }

    // Oppretter ny film
    @PostMapping
    public int lagFilm(@RequestBody Film film) {
        return filmService.lagNyFilm(film).getFilmnr();
    }

    // Dette oppdaterer
    @PutMapping("/{id}")
    public boolean oppdater(@PathVariable int id, @RequestBody Film film) {
        film.setFilmnr(id);
        return filmService.oppdaterFilm(film);
    }

    // Her blir en film slettet basert på ID
    @DeleteMapping("/{id}")
    public boolean slett(@PathVariable int id) {
        return filmService.slettFilm(id);
    }
}
