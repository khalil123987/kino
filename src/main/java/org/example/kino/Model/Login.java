package org.example.kino.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbllogin")
public class Login {

    @Id
    @Column(name = "l_brukernavn", length = 4)
    private String brukernavn;

    @Column(name = "l_pinkode", precision = 4, scale = 0)
    private Integer pinkode;

    @Column(name = "l_erplanlegger")
    private Boolean erPlanlegger;
}