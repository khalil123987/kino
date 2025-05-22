package org.example.kino.Controller;

import org.example.kino.Model.Plassbillett;
import org.example.kino.Model.Plassbillett.SammensattPlassBillettId;
import org.example.kino.Repository.PlassBillettRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Dette er controller for plassbillett og visr bare til de ulike
 * crud operasjonene + rest api
 * Definerer at du kan slette, putte, oppdatere via api link som man ser requestmapping
 */

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

        SammensattPlassBillettId id = new SammensattPlassBillettId(billettkode, radNr, seteNr, kinosalNr);
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

        SammensattPlassBillettId id = new SammensattPlassBillettId(billettkode, radNr, seteNr, kinosalNr);
        plassBillettRepository.deleteById(id);
    }
}
