package org.example.kino.Controller;

import org.example.kino.Model.Plass;
import org.example.kino.Service.PlassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plasser")
public class PlassController {

    private final PlassService plassService;

    @Autowired
    public PlassController(PlassService plassService) {
        this.plassService = plassService;
    }

    // Hent alle plasser i hele systemet
    @GetMapping
    public List<Plass> hentAllePlasser() {
        return plassService.hentAllePlasser();
    }

    // Hent alle plasser i en bestemt kinosal
    @GetMapping("/kinosal/{kinosalnr}")
    public List<Plass> hentPlasserIKinosal(@PathVariable int kinosalnr) {
        return plassService.hentPlasserIKinosal(kinosalnr);
    }

    // Hent en spesifikk plass
    @GetMapping("/{radnr}/{setenr}/{kinosalnr}")
    public Plass hentPlass(
            @PathVariable int radnr,
            @PathVariable int setenr,
            @PathVariable int kinosalnr
    ) {
        return plassService.hentPlass(radnr, setenr, kinosalnr);
    }

    // Lagre en ny plass
    @PostMapping
    public boolean lagPlass(@RequestBody Plass plass) {
        return plassService.lagPlass(plass);
    }

    // Slett en plass
    @DeleteMapping("/{radnr}/{setenr}/{kinosalnr}")
    public boolean slettPlass(
            @PathVariable int radnr,
            @PathVariable int setenr,
            @PathVariable int kinosalnr
    ) {
        return plassService.slettPlass(radnr, setenr, kinosalnr);
    }
}
