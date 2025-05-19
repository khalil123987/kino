package org.example.kino.Repository;

import org.example.kino.Model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {

    // Finn login basert på brukernavn
    Optional<Login> findByBrukernavn(String brukernavn);

    // Finn login basert på brukernavn og pinkode (for autentisering)
    Optional<Login> findByBrukernavnAndPinkode(String brukernavn, Integer pinkode);

    // Finn alle planleggere
    List<Login> findByErPlanleggerTrue();

    // Finn alle vanlige brukere (ikke planleggere)
    List<Login> findByErPlanleggerFalse();

    // Sjekk om en bruker eksisterer
    boolean existsByBrukernavn(String brukernavn);
}