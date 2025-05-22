//godkjent av khalil

package org.example.kino.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// Entity-klasse for kinosal som mapper til tblkinosal tabellen
@Data
@Entity
@Table(name = "tblkinosal")
public class Kinosal {

    // Primærnøkkel - unik ID for kinosalen
    @Id
    @Column(name = "k_kinosalnr")
    private Integer kinosalNr;

    // Navnet på kinoen (maks 20 tegn)
    @Column(name = "k_kinonavn", length = 20)
    private String kinoNavn;

    // Navnet på kinosalen (maks 20 tegn)
    @Column(name = "k_kinosalnavn", length = 20)
    private String kinosalNavn;

}