/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.ilucic.aplikacija_5.mvc;

import java.util.List;
import org.foi.nwtis.ilucic.aplikacija_4.ws.WsLetovi.endpoint.LetAviona;
import org.foi.nwtis.ilucic.aplikacija_4.ws.WsLetovi.endpoint.Letovi;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.xml.ws.WebServiceRef;

/**
 * Klasa KontrolerLetova koja poput klase KontrolerAerodroma kontorlira stranice i modele za letove
 * jspove.
 *
 * @author Ivan Lucić
 */
@Controller
@Path("letovi")
@RequestScoped
public class KontrolerLetova {

  @Context
  HttpServletRequest request;

  /** Modeli koji se injektiraju za jsp */
  @Inject
  private Models model;

  /** Kontekst koji se učitava */
  @Context
  ServletContext context;

  @WebServiceRef(wsdlLocation = "http://localhost:8080/ilucic_aplikacija_4/letovi?wsdl")
  private Letovi letoviService;


  /**
   * Metoda koja služi samo za određivanje početnog jsp-a.
   */
  @GET
  @View("letoviPocetna.jsp")
  public void pocetak() {}

  /**
   * Metoda koja služi samo za određivanje početnog jsp-a.
   */
  @GET
  @Path("pregled56")
  @View("letoviPocetna.jsp")
  public void pocetnaEkstra() {}

  @GET
  @Path("interval")
  @View("pregledLetovaInterval.jsp")
  public void jpaGetLetoveINterval(@QueryParam("icao") String icao,
      @QueryParam("vrijemeOd") String vrijemeOd, @QueryParam("vrijemeDo") String vrijemeDo,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    try {
      HttpSession session = request.getSession(false);
      if (session == null) {
        model.put("greska", "Niste prijavljeni u sustav!");
        return;
      }
      String korime = (String) session.getAttribute("korisnickoIme");
      String lozinka = (String) session.getAttribute("sifra");
      if (korime == null || lozinka == null) {
        model.put("greska", "Niste prijavljeni u sustav!");
        return;
      }
      if (icao.isEmpty() || vrijemeOd.isEmpty() || vrijemeDo.isEmpty()) {
        return;
      }
      var port = letoviService.getWsLetoviPort();
      List<LetAviona> letovi =
          port.dajPolaskeIntervala(korime, lozinka, icao, vrijemeOd, vrijemeDo, odBroja, broj);
      if (letovi != null) {
        model.put("letovi", letovi);
        return;
      } else {
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  @GET
  @Path("dan")
  @View("pregledLetovaDan.jsp")
  public void jpaGetLetoveINterval(@QueryParam("icao") String icao,
      @QueryParam("vrijeme") String vrijeme, @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    try {
      HttpSession session = request.getSession(false);
      if (session == null) {
        model.put("greska", "Niste prijavljeni u sustav!");
        return;
      }
      String korime = (String) session.getAttribute("korisnickoIme");
      String lozinka = (String) session.getAttribute("sifra");
      if (korime == null || lozinka == null) {
        model.put("greska", "Niste prijavljeni u sustav!");
        return;
      }
      if (icao.isEmpty() || vrijeme.isEmpty()) {
        return;
      }
      var port = letoviService.getWsLetoviPort();
      List<LetAviona> letovi = port.dajPolaskeNaDan(korime, lozinka, icao, vrijeme, odBroja, broj);
      if (letovi != null) {
        model.put("letovi", letovi);
        return;
      } else {
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  @GET
  @Path("OpenSky")
  @View("pregledLetovaOS.jsp")
  public void jpaGetLetoveINterval(@QueryParam("icao") String icao,
      @QueryParam("vrijeme") String vrijeme) {
    try {
      HttpSession session = request.getSession(false);
      if (session == null) {
        model.put("greska", "Niste prijavljeni u sustav!");
        return;
      }
      String korime = (String) session.getAttribute("korisnickoIme");
      String lozinka = (String) session.getAttribute("sifra");
      if (korime == null || lozinka == null) {
        model.put("greska", "Niste prijavljeni u sustav!");
        return;
      }
      if (icao.isEmpty() || vrijeme.isEmpty()) {
        return;
      }
      var port = letoviService.getWsLetoviPort();
      List<LetAviona> letovi = port.dajPolaskeNaDanOS(korime, lozinka, icao, vrijeme);
      if (letovi != null) {
        model.put("letovi", letovi);
        return;
      } else {
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

}
