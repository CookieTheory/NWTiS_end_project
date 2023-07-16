package org.foi.nwtis.ilucic.aplikacija_5.mvc;


import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.ilucic.aplikacija_4.ws.WsKorisnici.endpoint.Korisnici;
import org.foi.nwtis.ilucic.aplikacija_4.ws.WsKorisnici.endpoint.Korisnici_Service;
import org.foi.nwtis.ilucic.aplikacija_5.slusaci.Slusac2;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.xml.ws.WebServiceRef;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("korisnici")
@RequestScoped
public class KontrolerKorisnikaWeb {
  @Context
  HttpServletRequest request;

  Konfiguracija konfig;

  public KontrolerKorisnikaWeb() {
    konfig = Slusac2.getKonfig();
  }

  @WebServiceRef(wsdlLocation = "http://localhost:8080/ilucic_aplikacija_4/korisnici?wsdl")
  private Korisnici_Service service;

  @Inject
  private Models model;

  @GET
  @View("korisniciPocetna.jsp")
  public void pocetak() {}

  @GET
  @Path("registracija")
  @View("registracija.jsp")
  public void registracijaKorisnika() {}

  @POST
  @Path("registracija")
  @View("registracija.jsp")
  public void registracijaKorisnika(@FormParam("ime") String ime,
      @FormParam("prezime") String prezime, @FormParam("korisnickoIme") String korisnickoIme,
      @FormParam("sifra") String sifra) {
    if (ime == null || prezime == null || korisnickoIme == null || sifra == null) {
      return;
    }
    try {
      var port = service.getWsKorisniciPort();
      Korisnici korisnik = new Korisnici();
      korisnik.setIme(ime);
      korisnik.setPrezime(prezime);
      korisnik.setKorisnickoIme(korisnickoIme);
      korisnik.setSifra(sifra);
      boolean uspjeh = port.kreirajKorisnika(korisnik);
      if (uspjeh == true) {
        model.put("uspjeh", "Korisnik je uspješno dodan");
      } else {
        model.put("greska", "Greška s dodavanjem korisnika");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("prijava")
  @View("prijava.jsp")
  public void prijavaKorisnika() {}

  @POST
  @Path("prijava")
  @View("prijava.jsp")
  public void prijavaKorisnika(@FormParam("korisnickoIme") String korisnickoIme,
      @FormParam("sifra") String sifra) {
    if (korisnickoIme == null || sifra == null) {
      return;
    }
    try {
      var port = service.getWsKorisniciPort();
      boolean uspjeh = port.ulogirajKorisnika(korisnickoIme, sifra);
      if (uspjeh == true) {
        HttpSession session = request.getSession();
        session.setAttribute("korisnickoIme", korisnickoIme);
        session.setAttribute("sifra", sifra);
        model.put("uspjeh", "Korisnik je uspješno ulogiran");
      } else {
        model.put("greska", "Greška s prijavom korisnika");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("pregledKorisnika")
  @View("pregledKorisnika.jsp")
  public void pregledKorisnika() {}

  @POST
  @Path("pregledKorisnika")
  @View("pregledKorisnika.jsp")
  public void pregledKorisnika(@FormParam("traziImeKorisnika") String traziImeKorisnika,
      @FormParam("traziPrezimeKorisnika") String traziPrezimeKorisnika) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      model.put("greska", "Niste prijavljeni u sustav!");
      return;
    }
    String korime = (String) session.getAttribute("korisnickoIme");
    String lozinka = (String) session.getAttribute("sifra");
    var port = service.getWsKorisniciPort();
    List<Korisnici> listaKorisnika = new ArrayList<>();
    if (korime == null || lozinka == null) {
      model.put("greska", "Niste prijavljeni u sustav!");
      return;
    }
    listaKorisnika = port.dajKorisnike(korime, lozinka, traziImeKorisnika, traziPrezimeKorisnika);
    model.put("korisnici", listaKorisnika);
  }

}
