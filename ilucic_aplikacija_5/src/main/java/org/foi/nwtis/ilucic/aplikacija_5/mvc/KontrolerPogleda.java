/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.ilucic.aplikacija_5.mvc;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

/**
 * Klasa KontrolerLetova koja poput klase KontrolerAerodroma kontorlira stranice i modele za letove
 * jspove.
 *
 * @author Ivan Lucić
 */
@Controller
@Path("pocetna")
@RequestScoped
public class KontrolerPogleda {

  /** Modeli koji se injektiraju za jsp */
  @Inject
  private Models model;

  /** Kontekst koji se učitava */
  @Context
  ServletContext context;

  /**
   * Metoda koja služi samo za određivanje početnog jsp-a.
   */
  @GET
  @View("index.jsp")
  public void pocetak() {}

}
