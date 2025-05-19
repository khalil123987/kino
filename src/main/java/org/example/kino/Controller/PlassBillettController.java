package org.example.kino.Controller;

import org.example.kino.Model.Plassbillett;
import org.example.kino.Model.Plassbillett.sammensattPlassBillettId;
import org.example.kino.Repository.PlassBillettRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/plassbilletter")
public class PlassBillettController {

    @Autowired
    private PlassBillettRepository plassBillettRepository;

    @GetMapping
    public List<Plassbillett> getAllPlassBilletter() {
        return plassBillettRepository.findAll();
    }

    @GetMapping("/{billettkode}/{radNr}/{seteNr}/{kinosalNr}")
    public Optional<Plassbillett> getPlassBillett(
            @PathVariable String billettkode,
            @PathVariable int radNr,
            @PathVariable int seteNr,
            @PathVariable int kinosalNr) {

        sammensattPlassBillettId id = new sammensattPlassBillettId(billettkode, radNr, seteNr, kinosalNr);
        return plassBillettRepository.findById(id);
    }

    @PostMapping
    public Plassbillett createPlassBillett(@RequestBody Plassbillett plassBillett) {
        return plassBillettRepository.save(plassBillett);
    }

    @DeleteMapping("/{billettkode}/{radNr}/{seteNr}/{kinosalNr}")
    public void deletePlassBillett(
            @PathVariable String billettkode,
            @PathVariable int radNr,
            @PathVariable int seteNr,
            @PathVariable int kinosalNr) {

        sammensattPlassBillettId id = new sammensattPlassBillettId(billettkode, radNr, seteNr, kinosalNr);
        plassBillettRepository.deleteById(id);
    }
}
