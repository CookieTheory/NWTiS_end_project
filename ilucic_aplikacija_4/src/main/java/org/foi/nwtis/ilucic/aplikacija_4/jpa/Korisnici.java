package org.foi.nwtis.ilucic.aplikacija_4.jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;


/**
 * The persistent class for the KORISNICI database table.
 * 
 */
@Entity
@NamedQuery(name = "Korisnici.findAll", query = "SELECT k FROM Korisnici k")
public class Korisnici implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "DATUM_REGISTRACIJE")
  private Timestamp datumRegistracije;

  private String ime;

  @Column(name = "KORISNICKO_IME")
  private String korisnickoIme;

  private String prezime;

  private String sifra;

  public Korisnici() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Timestamp getDatumRegistracije() {
    return this.datumRegistracije;
  }

  public void setDatumRegistracije(Timestamp datumRegistracije) {
    this.datumRegistracije = datumRegistracije;
  }

  public String getIme() {
    return this.ime;
  }

  public void setIme(String ime) {
    this.ime = ime;
  }

  public String getKorisnickoIme() {
    return this.korisnickoIme;
  }

  public void setKorisnickoIme(String korisnickoIme) {
    this.korisnickoIme = korisnickoIme;
  }

  public String getPrezime() {
    return this.prezime;
  }

  public void setPrezime(String prezime) {
    this.prezime = prezime;
  }

  public String getSifra() {
    return this.sifra;
  }

  public void setSifra(String sifra) {
    this.sifra = sifra;
  }

}
