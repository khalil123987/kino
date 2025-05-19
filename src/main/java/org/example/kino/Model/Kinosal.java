package org.example.kino.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "tblkinosal")
public class Kinosal {

    @Id
    @Column(name = "k_kinosalnr")
    private Integer kinosalNr;

    @Column(name = "k_kinonavn", length = 20)
    private String kinoNavn;

    @Column(name = "k_kinosalnavn", length = 20)
    private String kinosalNavn;

}