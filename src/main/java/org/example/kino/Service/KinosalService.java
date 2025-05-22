//Gdokjent og testet av Khalil

package org.example.kino.Service;

import org.example.kino.Model.Kinosal;
import org.example.kino.Repository.KinosalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KinosalService {

    private final KinosalRepository kinosalRepository;

    @Autowired
    public KinosalService(KinosalRepository kinosalRepository) {
        this.kinosalRepository = kinosalRepository;
    }

    // Hent alle kinosaler
    public List<Kinosal> finnAlleKinosaler() {
        return kinosalRepository.findAll();
    }

    // Hent kinosal basert på ID
    public Optional<Kinosal> finnKinosalMedId(Integer kinosalNr) {
        return kinosalRepository.findById(kinosalNr);
    }

    // Hent kinosaler basert på kinonavn
    public List<Kinosal> finnKinosalMedKinonavn(String kinoNavn) {
        return kinosalRepository.findByKinoNavn(kinoNavn);
    }

    // Hent kinosal basert på kinonavn og kinosalnavn
    public Kinosal finnKinosalMedKinonavnOgSalnavn(String kinoNavn, String kinosalNavn) {
        return kinosalRepository.findByKinoNavnAndKinosalNavn(kinoNavn, kinosalNavn);
    }

    // Søk etter kinosaler med lignende navn
    public List<Kinosal> søkKinosalMedNavn(String navnDel) {
        return kinosalRepository.findByKinosalNavnContainingIgnoreCase(navnDel);
    }

    // Legge til eller oppdatere kinosal
    @Transactional
    public Kinosal lagreKinosal(Kinosal kinosal) {
        return kinosalRepository.save(kinosal);
    }

    // Slette kinosal
    @Transactional
    public void slettKinosal(Integer kinosalNr) {
        kinosalRepository.deleteById(kinosalNr);
    }

    // Sjekk om kinosal eksisterer
    public boolean kinosalEksisterer(Integer kinosalNr) {
        return kinosalRepository.existsById(kinosalNr);
    }

    // Telle antall kinosaler per kino
    public Long tellKinosaler(String kinoNavn) {
        return kinosalRepository.countByKinoNavn(kinoNavn);
    }
}