package org.example.kino.Service;

import org.example.kino.Model.Plass;
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

    public List<Plass> hentPlasserIKinosal(int kinosalnr) {
        return plassRepository.findAll();
    }

    public List<Plass>hentAllePlasser() {
        return plassRepository.findByPlassId(kinosalnr);
    }

    public Plass hentPlass(int radnr, int setenr, int kinosalnr) {
        return plassRepository.findById(radnr, setenr, kinosalnr);
    }

    public boolean lagPlass (Plass plass) {
        return plassRepository.save(plass);
    }

    public boolean slettPlass (int radnr, int setenr, int kinosalnr) {
        return plassRepository.delete(radnr, setenr, kinosalnr);
    }
}
