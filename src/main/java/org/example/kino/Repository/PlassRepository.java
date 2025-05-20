package org.example.kino.Repository;

import org.example.kino.Model.Plass;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlassRepository {

    List<Plass> findAll();
    List<Plass> findByPlassId(int id);
    List<Plass> findByPlassName(String plassName);
    List<Plass> findByPlassNameLike(String plassName);
    List<Plass> findByPlassNameStartsWith(String plassName);
    List<Plass> findByPlassNameEndsWith(String plassName);
    List<Plass> findByPlassNameContains(String plassName);
    List<Plass> findByPlassKinonrContains(String plassKinonr);
    List<Plass> findByPlassKinonrLike(String plassKinonr);


    boolean save(Plass plass);
    boolean delete(Plass plass);
    boolean delete(int radnr, int setenr, int kinosalnr);
    boolean update(Plass plass);

    Plass findById(int radnr, int setenr, int kinosalnr);
    Plass findByPlassKinonr(String plassKinonr);
}
