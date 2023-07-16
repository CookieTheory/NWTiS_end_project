package org.foi.nwtis.ilucic.aplikacija_4.jpa;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;


/**
 * The persistent class for the AERODROMI_LETOVI database table.
 * 
 */
@Entity
@Table(name = "AERODROMI_LETOVI")
@NamedQuery(name = "AerodromiLetovi.findAll", query = "SELECT a FROM AerodromiLetovi a")
public class AerodromiLetovi implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private boolean status;

  // bi-directional many-to-one association to Airport
  @ManyToOne
  @JoinColumn(name = "ICAO")
  private Airports airport;

  public AerodromiLetovi() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean getStatus() {
    return this.status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public Airports getAirport() {
    return this.airport;
  }

  public void setAirport(Airports airport) {
    this.airport = airport;
  }

}
