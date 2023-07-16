/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.ilucic.aplikacija_5.mvc;

import org.foi.nwtis.ilucic.aplikacija_5.rest.RestKlijentDnevnika;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;

/**
 * Klasa KontrolerAerodroma, služi za kontrolu koju jspovi se koriste za koji path i također stavlja
 * model koji primi natrag za jspove.
 *
 * @author Ivan Lucić
 */
@Controller
@Path("dnevnik")
@RequestScoped
public class KontrolerDnevnika {

  /** Modeli koji se injektiraju za jsp */
  @Inject
  private Models model;

  /** Kontekst koji se učitava. */
  @Context
  ServletContext context;

  @GET
  @View("dnevnik.jsp")
  public void getDnevnikZapisi(@PathParam("icaoOd") String icaoOd,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj, @QueryParam("vrsta") String vrsta) {
    try {
      RestKlijentDnevnika rcd = new RestKlijentDnevnika(context);
      var zapisi = rcd.getDnevnikZapisi(odBroja, broj, vrsta);
      model.put("zapisi", zapisi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
