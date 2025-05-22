// Testet og implementert av Sahil
package org.example.kino.Service;

import org.example.kino.Model.Film;
import org.example.kino.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Angir at dette er en service-komponent
public class FilmService {

    private final FilmRepository filmRepository;

    @Autowired // Injiserer FilmRepository automatisk via konstruktøren
    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    // Henter alle filmer fra databasen
    public List<Film> hentAlleFilmer() {
        return (List<Film>) filmRepository.findAll();
    }

    // Henter en film basert på ID
    public Optional<Film> hentFilmVedId(int id) {
        return filmRepository.findById(id);
    }

    // Lager en ny film og lagrer den i databasen
    public Film lagNyFilm(Film film) {
        return filmRepository.save(film);
    }

    // Oppdaterer en eksisterende film hvis den finnes
    public boolean oppdaterFilm(Film film) {
        if (filmRepository.existsById(film.getFilmnr())) {
            filmRepository.save(film);
            return true;
        }
        return false;
    }

    // Sletter en film hvis den finnes
    public boolean slettFilm(int id) {
        if (filmRepository.existsById(id)) {
            filmRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
