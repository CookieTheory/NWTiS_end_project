package org.foi.nwtis.ilucic.aplikacija_2.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.NadzorKlasa;
import com.google.gson.Gson;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestAerodromi, služi za komunikaciju s prvom aplikacijom u dohvaćanju podataka oko stanja
 * itd.
 *
 * @author Ivan Lucić
 */
@Path("nadzor")
@RequestScoped
public class RestAplikacija1 {

  @Context
  ServletContext context;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response saljiStatus() {
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");

    String adresa = konfig.dajPostavku("aplikacija_1.adresa");
    int vrata = Integer.parseInt(konfig.dajPostavku("aplikacija_1.vrata"));
    Socket uticnica = povezi(adresa, vrata);
    String poruka = posaljiPoruku("STATUS", uticnica);

    var gson = new Gson();
    Response odgovor;
    if (poruka.contains("OK")) {
      var nadzorObjekt = new NadzorKlasa(200, poruka);
      var jsonOdgovor = gson.toJson(nadzorObjekt);
      odgovor = Response.ok().entity(jsonOdgovor).build();
    } else {
      var nadzorObjekt = new NadzorKlasa(400, poruka);
      var jsonOdgovor = gson.toJson(nadzorObjekt);
      odgovor = Response.status(400).entity(jsonOdgovor).build();
    }

    return odgovor;
  }

  @GET
  @Path("{komanda}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response saljiKomandu(@PathParam("komanda") String komanda) {
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");

    String adresa = konfig.dajPostavku("aplikacija_1.adresa");
    int vrata = Integer.parseInt(konfig.dajPostavku("aplikacija_1.vrata"));
    Socket uticnica = povezi(adresa, vrata);
    String poruka = posaljiPoruku(komanda, uticnica);

    var gson = new Gson();
    Response odgovor;
    if (poruka.contains("OK")) {
      var nadzorObjekt = new NadzorKlasa(200, poruka);
      var jsonOdgovor = gson.toJson(nadzorObjekt);
      odgovor = Response.ok().entity(jsonOdgovor).build();
    } else {
      var nadzorObjekt = new NadzorKlasa(400, poruka);
      var jsonOdgovor = gson.toJson(nadzorObjekt);
      odgovor = Response.status(400).entity(jsonOdgovor).build();
    }

    return odgovor;
  }

  @GET
  @Path("INFO/{vrsta}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response saljiInfo(@PathParam("vrsta") String vrsta) {
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");

    String adresa = konfig.dajPostavku("aplikacija_1.adresa");
    int vrata = Integer.parseInt(konfig.dajPostavku("aplikacija_1.vrata"));
    Socket uticnica = povezi(adresa, vrata);
    String poruka = posaljiPoruku("INFO " + vrsta, uticnica);

    var gson = new Gson();
    Response odgovor;
    if (poruka.contains("OK")) {
      var nadzorObjekt = new NadzorKlasa(200, poruka);
      var jsonOdgovor = gson.toJson(nadzorObjekt);
      odgovor = Response.ok().entity(jsonOdgovor).build();
    } else {
      var nadzorObjekt = new NadzorKlasa(400, poruka);
      var jsonOdgovor = gson.toJson(nadzorObjekt);
      odgovor = Response.status(400).entity(jsonOdgovor).build();
    }

    return odgovor;
  }

  private Socket povezi(String adresa, int vrata) {
    Socket uticnica = null;
    try {
      uticnica = new Socket(adresa, vrata);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return uticnica;
  }

  private String posaljiPoruku(String komanda, Socket mreznaUticnica) {
    StringBuilder poruka = new StringBuilder();
    try {
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));
      pisac.write(komanda);
      pisac.newLine();
      pisac.flush();
      mreznaUticnica.shutdownOutput();
      while (true) {
        var redak = citac.readLine();
        if (redak == null)
          break;
        poruka.append(redak);
      }
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return poruka.toString();
  }

}
