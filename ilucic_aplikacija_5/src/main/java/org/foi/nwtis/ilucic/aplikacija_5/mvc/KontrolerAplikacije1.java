/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.ilucic.aplikacija_5.mvc;

import org.foi.nwtis.ilucic.aplikacija_5.rest.RestKlijentAplikacije1;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;

/**
 * Klasa KontrolerAerodroma, služi za kontrolu koju jspovi se koriste za koji path i također stavlja
 * model koji primi natrag za jspove.
 *
 * @author Ivan Lucić
 */
@Controller
@Path("pregled53")
@RequestScoped
public class KontrolerAplikacije1 {

  /** Modeli koji se injektiraju za jsp */
  @Inject
  private Models model;

  /** Kontekst koji se učitava. */
  @Context
  ServletContext context;

  /**
   * Metoda koja služi samo za određivanje početnog jsp-a.
   */
  @GET
  @View("aplikacija_1.jsp")
  public void pocetak() {}

  /**
   */
  @GET
  @Path("nadzor")
  @View("aplikacija_1.jsp")
  public void saljiStatus() {
    try {
      RestKlijentAplikacije1 rc1 = new RestKlijentAplikacije1(context);
      String odgovor;
      odgovor = rc1.getStatus();
      model.put("odgovor", odgovor);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("nadzor/{komanda}")
  @View("aplikacija_1.jsp")
  public void saljiKomandu(@PathParam("komanda") String komanda) {
    try {
      RestKlijentAplikacije1 rc1 = new RestKlijentAplikacije1(context);
      String odgovor;
      odgovor = rc1.getKomanda(komanda);
      model.put("odgovor", odgovor);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("nadzor/INFO/{komanda}")
  @View("aplikacija_1.jsp")
  public void saljiInfo(@PathParam("komanda") String komanda) {
    try {
      RestKlijentAplikacije1 rc1 = new RestKlijentAplikacije1(context);
      String odgovor;
      odgovor = rc1.getInfo("INFO", komanda);
      model.put("odgovor", odgovor);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }



}
