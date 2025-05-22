// Godkjent av Khalil

package org.example.kino.Service;

import org.example.kino.Model.Login;
import org.example.kino.Repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    private final LoginRepository loginRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    // Hent alle brukere
    public List<Login> hentAlleBrukere() {
        return loginRepository.findAll();
    }

    // Hent bruker basert på brukernavn
    public Login hentBruker(String brukernavn) {
        return loginRepository.findByBrukernavn(brukernavn).orElse(null);
    }

    // Autentisering - sjekk brukernavn og pinkode
    public Login autentiserBruker(String brukernavn, Integer pinkode) {
        return loginRepository.findByBrukernavnAndPinkode(brukernavn, pinkode).orElse(null);
    }

    // Lagre eller oppdater bruker
    public Login lagreBruker(Login login) {
        return loginRepository.save(login);
    }

    // Slett bruker
    public void slettBruker(String brukernavn) {
        loginRepository.deleteById(brukernavn);
    }

    // Sjekk om brukeren er planlegger
    public boolean erPlanlegger(String brukernavn) {
        Login bruker = hentBruker(brukernavn);
        return bruker != null && bruker.getErPlanlegger();
    }
}