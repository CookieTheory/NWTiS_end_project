package org.foi.nwtis.ilucic.aplikacija_4.ws;

import java.util.List;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.Korisnici;
import org.foi.nwtis.ilucic.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.podaci.PogresnaAutentikacija;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "korisnici")
public class WsKorisnici {

  @Inject
  KorisniciFacade KorisniciFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;


  @WebMethod
  public List<Korisnici> dajKorisnike(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka,
      @WebParam(name = "traziImeKorisnika") String traziImeKorisnika,
      @WebParam(name = "traziPrezimeKorisnika") String traziPrezimeKorisnika) {
    Korisnici provjera;
    try {
      provjera = KorisniciFacade.pronadiKorisnikaZaLoginIliProvjeru(korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      System.out.println(e.getMessage());
      return null;
    }
    if (provjera == null) {
      return null;
    }
    if (traziImeKorisnika.isEmpty() && traziPrezimeKorisnika.isEmpty()) {
      List<Korisnici> cijelaLista = KorisniciFacade.findAll();
      return cijelaLista;
    } else {
      List<Korisnici> filtriranaLista =
          KorisniciFacade.dajKorisnike(traziImeKorisnika, traziPrezimeKorisnika);
      return filtriranaLista;
    }
  }



  @WebMethod
  public Korisnici dajKorisnika(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka,
      @WebParam(name = "traziKorisnika") String traziKorisnika) {
    Korisnici provjera;
    try {
      provjera = KorisniciFacade.pronadiKorisnikaZaLoginIliProvjeru(korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      System.out.println(e.getMessage());
      return null;
    }
    if (provjera != null) {
      Korisnici pronaden = KorisniciFacade.dajKorisnika(traziKorisnika);
      if (pronaden != null) {
        return pronaden;
      } else {
        return null;
      }
    } else {
      return null;
    }


  }

  @WebMethod
  public boolean ulogirajKorisnika(@WebParam(name = "korime") String korime,
      @WebParam(name = "sifra") String sifra) {
    Korisnici pronaden;
    try {
      pronaden = KorisniciFacade.pronadiKorisnikaZaLoginIliProvjeru(korime, sifra);
    } catch (PogresnaAutentikacija e) {
      System.out.println(e.getMessage());
      return false;
    }

    if (pronaden != null) {
      return true;
    } else
      return false;
  }

  @WebMethod
  public boolean kreirajKorisnika(@WebParam(name = "korisnik") Korisnici korisnik) {

    boolean kreiran = KorisniciFacade.kreirajKorisnika(korisnik);

    return kreiran;
  }

}
