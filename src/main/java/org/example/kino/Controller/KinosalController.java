package org.example.kino.Controller;

import org.example.kino.Model.Kinosal;
import org.example.kino.Repository.KinosalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kinosaler")
public class KinosalController {

    private final KinosalRepository kinosalRepository;

    @Autowired
    public KinosalController(KinosalRepository kinosalRepository) {
        this.kinosalRepository = kinosalRepository;
    }

    // Hent alle kinosaler
    @GetMapping
    public List<Kinosal> hentAlleKinosaler() {
        return kinosalRepository.findAll();
    }

    // Hent kinosal med ID
    @GetMapping("/{kinosalNr}")
    public Kinosal hentKinosal(@PathVariable Integer kinosalNr) {
        return kinosalRepository.findById(kinosalNr).orElse(null);
    }

    // Hent kinosaler basert på kinonavn
    @GetMapping("/kino/{kinoNavn}")
    public List<Kinosal> hentKinosalMedKinonavn(@PathVariable String kinoNavn) {
        return kinosalRepository.findByKinoNavn(kinoNavn);
    }

    // Opprett eller oppdater kinosal
    @PostMapping
    public Kinosal lagreKinosal(@RequestBody Kinosal kinosal) {
        return kinosalRepository.save(kinosal);
    }

    // Slett kinosal
    @DeleteMapping("/{kinosalNr}")
    public void slettKinosal(@PathVariable Integer kinosalNr) {
        kinosalRepository.deleteById(kinosalNr);
    }
}