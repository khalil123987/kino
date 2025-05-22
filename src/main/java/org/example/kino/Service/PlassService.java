// Testet og implementert av Sahil
package org.example.kino.Service;

import org.example.kino.Model.Plass;
import org.example.kino.Model.Plass.PlassId;
import org.example.kino.Repository.PlassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Merk denne klassen som en service komponent i Spring
public class PlassService {

    private final PlassRepository plassRepository;

    @Autowired // Injiserer PlassRepository via konstruktøren
    public PlassService(PlassRepository plassRepository) {
        this.plassRepository = plassRepository;
    }

    // Henter alle plasser i systemet
    public List<Plass> hentAllePlasser() {
        return plassRepository.findAll();
    }

    // Henter alle plasser i en bestemt kinosal basert på kinosalnummer
    public List<Plass> hentPlasserIKinosal(int kinosalnr) {
        return plassRepository.findByKinosalNr(kinosalnr);
    }

    // Henter en spesifikk plass basert på radnummer, setenummer og kinosalnummer
    public Plass hentPlass(int radnr, int setenr, int kinosalnr) {
        PlassId id = new PlassId(radnr, setenr, kinosalnr); // Komposittnøkkel
        return plassRepository.findById(id).orElse(null);  // Returner plass eller null hvis ikke funnet
    }

    // Lagrer en ny plass i databasen
    public boolean lagPlass(Plass plass) {
        plassRepository.save(plass);
        return true;
    }

    // Sletter en plass basert på radnummer, setenummer og kinosalnummer
    public boolean slettPlass(int radnr, int setenr, int kinosalnr) {
        PlassId id = new PlassId(radnr, setenr, kinosalnr);
        if (plassRepository.existsById(id)) {
            plassRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
