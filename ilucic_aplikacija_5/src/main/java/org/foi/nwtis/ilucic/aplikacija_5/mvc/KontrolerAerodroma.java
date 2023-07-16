/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.ilucic.aplikacija_5.mvc;

import java.util.List;
import org.foi.nwtis.ilucic.aplikacija_4.ws.WsAerodromi.endpoint.Aerodromi;
import org.foi.nwtis.ilucic.aplikacija_4.ws.WsMeteo.endpoint.Meteo;
import org.foi.nwtis.ilucic.aplikacija_5.rest.RestKlijentAerodroma;
import org.foi.nwtis.podaci.Aerodrom;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.ws.WebServiceRef;

/**
 * Klasa KontrolerAerodroma, služi za kontrolu koju jspovi se koriste za koji path i također stavlja
 * model koji primi natrag za jspove.
 *
 * @author Ivan Lucić
 */
@Controller
@Path("aerodromi")
@RequestScoped
public class KontrolerAerodroma {

  @Context
  HttpServletRequest request;

  @Context
  HttpServletResponse response;

  /** Modeli koji se injektiraju za jsp */
  @Inject
  private Models model;

  /** Kontekst koji se učitava. */
  @Context
  ServletContext context;

  @WebServiceRef(wsdlLocation = "http://localhost:8080/ilucic_aplikacija_4/meteo?wsdl")
  private Meteo service;

  @WebServiceRef(wsdlLocation = "http://localhost:8080/ilucic_aplikacija_4/aerodromi?wsdl")
  private Aerodromi aerodromiService;

  /**
   * Metoda koja služi samo za određivanje početnog jsp-a.
   */
  @GET
  @View("aerodromiPocetna.jsp")
  public void pocetak() {}


  /**
   * Metoda koja služi samo za određivanje početnog jsp-a.
   */
  @GET
  @Path("pregled55")
  @View("aerodromiPocetna.jsp")
  public void pocetnaEkstra() {}

  @GET
  @Path("svi")
  @View("aerodromi.jsp")
  public void getAerodromi(@QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj, @QueryParam("naziv") String traziNaziv,
      @QueryParam("drzava") String traziDrzavu) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma(context);
      List<Aerodrom> aerodromi;
      aerodromi = rca.getAerodromi(odBroja, broj, traziNaziv, traziDrzavu);
      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("{icao}")
  @View("aerodromMeteo.jsp")
  public void getAerodromMeteo(@PathParam("icao") String icao) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma(context);
      var aerodrom = rca.getAerodrom(icao);
      model.put("aerodrom", aerodrom);
      var port = service.getWsMeteoPort();
      var meteoPodaci = port.dajMeteo(icao);
      model.put("meteo", meteoPodaci);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Metoda get2AerodromaUdaljenosti dohvaća aerodrome iz RestKlijentAerodroma klase. Šalje icaoOd i
   * icaoDo za određivanje između koja dva aerodroma se uzima udaljenost.
   *
   * @param icaoOd - varijabla icaoOd koja određuje od kojeg aerodroma.
   * @param icaoDo - varijabla icaoDo koja određuje do kojeg aerodroma.
   * @return Vraća model udaljenosti za jsp.
   */
  @GET
  @Path("{icaoOd}/{icaoDo}")
  @View("aerodromiUdaljenosti.jsp")
  public void get2AerodromaUdaljenosti(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma(context);
      var udaljenosti = rca.get2AerodromaUdaljenosti(icaoOd, icaoDo);
      model.put("udaljenosti", udaljenosti);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @GET
  @Path("{icaoOd}/izracunaj/{icaoDo}")
  @View("aerodromiIzracunaj.jsp")
  public void get2AerodromaIzracunaj(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma(context);
      var udaljenost = rca.get2AerodromaIzracunaj(icaoOd, icaoDo);
      model.put("udaljenost", udaljenost);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("{icaoOd}/udaljenost1/{icaoDo}")
  @View("aerodromiIcaoOdUdaljenost1IcaoDo.jsp")
  public void get2AerodromaUdaljenost1(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma(context);
      var udaljenosti = rca.get2AerodromaUdaljenost1(icaoOd, icaoDo);
      model.put("udaljenosti", udaljenosti);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("{icaoOd}/udaljenost2")
  @View("aerodromiIcaoOdUdaljenost2.jsp")
  public void getAerodromUdaljenosti2(@PathParam("icaoOd") String icaoOd,
      @QueryParam("drzava") String drzava, @QueryParam("km") float km) {
    try {
      if (drzava != null && !drzava.isEmpty() && km > 0) {
        RestKlijentAerodroma rca = new RestKlijentAerodroma(context);
        var udaljenosti = rca.getAerodromUdaljenosti2(icaoOd, drzava, km);
        model.put("udaljenosti", udaljenosti);
      } else {
        String greska = "Fale query parametri.";
        model.put("greska", greska);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("{icao}/preuzimanje")
  @Produces(MediaType.APPLICATION_JSON)
  public Response jpaDodajAerodromZaPreuzimanje(@PathParam("icao") String icao) {
    try {
      HttpSession session = request.getSession(false);
      if (session == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      String korime = (String) session.getAttribute("korisnickoIme");
      String lozinka = (String) session.getAttribute("sifra");
      if (korime == null || lozinka == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      var port = aerodromiService.getWsAerodromiPort();
      boolean dodano = port.dodajAerodromZaLetove(korime, lozinka, icao);
      if (dodano == true) {
        return Response.ok().build();
      } else {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }



}
