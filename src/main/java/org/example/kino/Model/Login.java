//godkjent av khalil

package org.example.kino.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entity-klasse for brukerinnlogging som mapper til tbllogin tabellen
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbllogin")
public class Login {

    // Primærnøkkel - brukernavn (maks 4 tegn)
    @Id
    @Column(name = "l_brukernavn", length = 4)
    private String brukernavn;

    // PIN-kode for innlogging (4 siffer)
    @Column(name = "l_pinkode", precision = 4, scale = 0)
    private Integer pinkode;

    // Om brukeren har planlegger-rettigheter
    @Column(name = "l_erplanlegger")
    private Boolean erPlanlegger;
}