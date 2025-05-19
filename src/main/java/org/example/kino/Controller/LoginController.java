package org.example.kino.Controller;

import org.example.kino.Model.Login;
import org.example.kino.Repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logins")
public class LoginController {

    private final LoginRepository loginRepository;

    @Autowired
    public LoginController(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    // Hent alle brukere
    @GetMapping
    public List<Login> hentAlleBrukere() {
        return loginRepository.findAll();
    }

    // Hent bruker med brukernavn
    @GetMapping("/{brukernavn}")
    public Login hentBruker(@PathVariable String brukernavn) {
        return loginRepository.findByBrukernavn(brukernavn).orElse(null);
    }

    // Hent alle planleggere
    @GetMapping("/planleggere")
    public List<Login> hentPlanleggere() {
        return loginRepository.findByErPlanleggerTrue();
    }

    // Opprett eller oppdater bruker
    @PostMapping
    public Login lagreBruker(@RequestBody Login login) {
        return loginRepository.save(login);
    }

    // Slett bruker
    @DeleteMapping("/{brukernavn}")
    public void slettBruker(@PathVariable String brukernavn) {
        loginRepository.deleteById(brukernavn);
    }

    // Enkel autentisering
    @PostMapping("/autentiser")
    public Login autentiserBruker(@RequestParam String brukernavn, @RequestParam Integer pinkode) {
        return loginRepository.findByBrukernavnAndPinkode(brukernavn, pinkode).orElse(null);
    }
}