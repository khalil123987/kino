package org.example.kino.Service;

/**
 * Service for mellomledd mellom repository og controller
 * logikk
 */

import org.example.kino.Model.Visning;
import org.example.kino.Repository.VisningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisningService {

    @Autowired
    private VisningRepository visningRepository;

    public List<Visning> finnAlleVisninger() {
        return visningRepository.findAll();
    }

    public Optional<Visning> finnVisningMedId(int id) {
        return visningRepository.findById(id);
    }

    public Visning opprettVisning(Visning visning) {
        return visningRepository.save(visning);
    }
// Du kan tenke deg denne henger smamen med det jeg forklarte i en av de andre filene
    // Det med at du definerer for å oppfylle en betingelse - service og controller henger sammen
    public Visning oppdaterVisning(int id, Visning oppdatert) {
        return visningRepository.findById(id).map(visning -> {
            visning.setFilmNr(oppdatert.getFilmNr());
            visning.setKinosalNr(oppdatert.getKinosalNr());
            visning.setTidspunkt(oppdatert.getTidspunkt());
            return visningRepository.save(visning);
        }).orElseThrow(() -> new RuntimeException("Fant ikke visning med id: " + id));
    }

    public void slettVisning(int id) {
        visningRepository.deleteById(id);
    }
}
