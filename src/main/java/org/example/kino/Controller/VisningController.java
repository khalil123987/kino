package org.example.kino.Controller;

import org.example.kino.Model.Visning;
import org.example.kino.Repository.VisningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Testet med postman av Sarmad
 * Disse fungerer med annen kode som relateres
 *
 */

@RestController
@RequestMapping("/api/visninger")
public class VisningController {

    @Autowired
    private VisningRepository visningRepository;

    @GetMapping
    public List<Visning> getAllVisninger() {
        return visningRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Visning> getVisningById(@PathVariable Integer id) {
        return visningRepository.findById(id);
    }

    @PostMapping
    public Visning createVisning(@RequestBody Visning visning) {
        return visningRepository.save(visning);
    }
                /**
              Her endres ved at variabler defineres mot at alle betingelser er oppfylt
                 */

    @PutMapping("/{id}")
    public Visning updateVisning(@PathVariable Integer id, @RequestBody Visning updated) {
        return visningRepository.findById(id)
                .map(visning -> {
                    visning.setFilmNr(updated.getFilmNr());
                    visning.setKinosalNr(updated.getKinosalNr());
                    visning.setTidspunkt(updated.getTidspunkt());
                    return visningRepository.save(visning);
                })
                .orElseThrow(() -> new RuntimeException("Visning ikke funnet med id: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteVisning(@PathVariable Integer id) {
        visningRepository.deleteById(id);
    }
}
