package org.example.kino.Repository;

import org.example.kino.Model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repo for systemdmin - her kreves flere queries
 * Noen queries lagde vi selv men måtte oppdatere med KI for at vi hadde noe feil struktur eller at
 * postgres/intellij ikke godtar på en spesifik måte
 * Ellers var tankegangen vår
 */
public interface SystemAdminRepository extends JpaRepository<Login, String> {

    Optional<Login> findByBrukernavnAndPinkode(String brukernavn, Integer pinkode);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Login l WHERE l.brukernavn = ?1")
    boolean brukernavnEksisterer(String brukernavn);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Login l WHERE l.brukernavn = ?1 AND l.pinkode = ?2")
    boolean kanLoggeInn(String brukernavn, int pinkode);

    @Query("""
        SELECT CASE WHEN COUNT(sa) > 0 THEN true ELSE false END
        FROM Login sa
        WHERE sa.brukernavn = :brukernavn AND sa.pinkode = :pinkode AND sa.erPlanlegger = true
    """)
    boolean kanLoggeInnSomPlanlegger(
            @Param("brukernavn") String brukernavn,
            @Param("pinkode") int pinkode
    );

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO kino.tbllogin (l_brukernavn, l_pinkode, l_erplanlegger)
        VALUES (?1, ?2, ?3)
    """, nativeQuery = true)
    void opprettBruker(String brukernavn, int pinkode, boolean erPlanlegger);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM kino.tbllogin WHERE l_brukernavn = ?1", nativeQuery = true)
    void slettBruker(String brukernavn);

    @Modifying
    @Transactional
    @Query(value = "UPDATE kino.tbllogin SET l_pinkode = ?2 WHERE l_brukernavn = ?1", nativeQuery = true)
    void endrePin(String brukernavn, int nyPin);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM kino.tblvisning
        WHERE (v_dato + v_starttid) <= CURRENT_TIMESTAMP - INTERVAL '1 hour' * ?1
    """, nativeQuery = true)
    int slettVisningerEldreEnnXTimer(int timer);
}

/**
 * Trengte spørringer og data for å både innhente data om ubetalte/betalte billetter
 * fra billet men også visning (tiden)
 */