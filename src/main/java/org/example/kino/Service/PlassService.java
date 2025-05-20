package org.example.kino.Service;

import org.example.kino.Model.Plass;
import org.example.kino.Model.Plass.PlassId;
import org.example.kino.Repository.PlassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlassService {

    private final PlassRepository plassRepository;

    @Autowired
    public PlassService(PlassRepository plassRepository) {
        this.plassRepository = plassRepository;
    }

    public List<Plass> hentAllePlasser() {
        return plassRepository.findAll();
    }

    public List<Plass> hentPlasserIKinosal(int kinosalnr) {
        return plassRepository.findByKinosalNr(kinosalnr);
    }

    public Plass hentPlass(int radnr, int setenr, int kinosalnr) {
        PlassId id = new PlassId(radnr, setenr, kinosalnr);
        return plassRepository.findById(id).orElse(null);
    }

    public boolean lagPlass(Plass plass) {
        plassRepository.save(plass);
        return true;
    }

    public boolean slettPlass(int radnr, int setenr, int kinosalnr) {
        PlassId id = new PlassId(radnr, setenr, kinosalnr);
        if (plassRepository.existsById(id)) {
            plassRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
