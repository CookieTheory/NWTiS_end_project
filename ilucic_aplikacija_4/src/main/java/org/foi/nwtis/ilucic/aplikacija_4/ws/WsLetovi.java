package org.foi.nwtis.ilucic.aplikacija_4.ws;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.Airports;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.Korisnici;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.LetoviPolasci;
import org.foi.nwtis.ilucic.aplikacija_4.slusaci.Slusac;
import org.foi.nwtis.ilucic.aplikacija_4.zrna.AirportFacade;
import org.foi.nwtis.ilucic.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.ilucic.aplikacija_4.zrna.LetoviPolasciFacade;
import org.foi.nwtis.podaci.PogresnaAutentikacija;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.LetAviona;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "letovi")
public class WsLetovi {

  @Inject
  LetoviPolasciFacade letoviPolasciFacade;

  @Inject
  KorisniciFacade korisniciFacade;

  @Inject
  AirportFacade airportFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @WebMethod
  public List<LetAviona> dajPolaskeIntervala(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao,
      @WebParam(name = "danOd") String danOd, @WebParam(name = "danDo") String danDo,
      @WebParam(name = "odBroja") int odBroja, @WebParam(name = "broj") int broj) {
    Korisnici provjera;
    try {
      provjera = korisniciFacade.pronadiKorisnikaZaLoginIliProvjeru(korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      System.out.println(e.getMessage());
      return null;
    }
    if (provjera != null) {
      Airports aerodrom = airportFacade.find(icao);
      List<LetAviona> oblikLetAviona = new ArrayList<>();
      List<LetoviPolasci> avioni =
          letoviPolasciFacade.getLetoviIcaoIVrijeme(aerodrom, danOd, danDo, odBroja - 1, broj);
      if (avioni != null) {
        for (LetoviPolasci a : avioni) {
          LetAviona novi = new LetAviona(a.getIcao24(), a.getFirstSeen(), a.getAirport().getIcao(),
              a.getLastSeen(), a.getEstArrivalAirport(), a.getCallsign(),
              a.getEstDepartureAirportHorizDistance(), a.getEstDepartureAirportVertDistance(),
              a.getEstArrivalAirportHorizDistance(), a.getEstArrivalAirportVertDistance(),
              a.getDepartureAirportCandidatesCount(), a.getArrivalAirportCandidatesCount());
          oblikLetAviona.add(novi);
        }
        return oblikLetAviona;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @WebMethod
  public List<LetAviona> dajPolaskeNaDan(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao,
      @WebParam(name = "dan") String dan, @WebParam(name = "odBroja") int odBroja,
      @WebParam(name = "broj") int broj) {
    Korisnici provjera;
    try {
      provjera = korisniciFacade.pronadiKorisnikaZaLoginIliProvjeru(korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      System.out.println(e.getMessage());
      return null;
    }
    if (provjera != null) {
      Airports aerodrom = airportFacade.find(icao);
      List<LetAviona> oblikLetAviona = new ArrayList<>();
      List<LetoviPolasci> avioni =
          letoviPolasciFacade.getLetoviVrijeme(aerodrom, dan, odBroja - 1, broj);
      if (avioni != null) {
        for (LetoviPolasci a : avioni) {
          LetAviona novi = new LetAviona(a.getIcao24(), a.getFirstSeen(), a.getAirport().getIcao(),
              a.getLastSeen(), a.getEstArrivalAirport(), a.getCallsign(),
              a.getEstDepartureAirportHorizDistance(), a.getEstDepartureAirportVertDistance(),
              a.getEstArrivalAirportHorizDistance(), a.getEstArrivalAirportVertDistance(),
              a.getDepartureAirportCandidatesCount(), a.getArrivalAirportCandidatesCount());
          oblikLetAviona.add(novi);
        }
        return oblikLetAviona;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @WebMethod
  public List<LetAviona> dajPolaskeNaDanOS(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao,
      @WebParam(name = "dan") String dan) {
    Korisnici provjera;
    try {
      provjera = korisniciFacade.pronadiKorisnikaZaLoginIliProvjeru(korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      System.out.println(e.getMessage());
      return null;
    }
    if (provjera != null) {
      Konfiguracija konfig = Slusac.getKonfig();
      String korisnikOS = konfig.dajPostavku("OpenSkyNetwork.korisnik");
      String lozinkaOS = konfig.dajPostavku("OpenSkyNetwork.lozinka");
      long odVremena = konvertirajOd(dan);
      long doVremena = konvertirajDo(dan);
      OSKlijent oSKlijent = new OSKlijent(korisnikOS, lozinkaOS);

      List<LetAviona> avioniPolasci = null;
      try {
        System.out.println(
            "Dohvacam podatke za vrijeme od: " + odVremena + " , te za vrijeme do: " + doVremena);
        avioniPolasci = oSKlijent.getDepartures(icao, odVremena, doVremena);
      } catch (NwtisRestIznimka e) {
        e.printStackTrace();
      }
      return avioniPolasci;
    } else {
      return null;
    }
  }


  /**
   * Metoda konvertirajOd konvertira string vremena formata dd.MM.yyyy., splita ga i konvertira na
   * početak dana i dodaje sat vremena jer je autorova vremenska zona +2.
   *
   * @param vrijeme - varijabla vremena u obliku dd.MM.yyyy.
   * @return long podatak unix vremena
   */
  private long konvertirajOd(String vrijeme) {
    String[] odsjeceno = vrijeme.split("\\.");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date date;
    try {
      date = sdf.parse(odsjeceno[0] + "/" + odsjeceno[1] + "/" + odsjeceno[2]);
    } catch (java.text.ParseException e) {
      return 0;
    }
    long epoch = date.getTime();
    long time = (epoch / 1000) + 2 * 3600;
    return time;
  }

  /**
   * Metoda konvertirajOd konvertira string vremena formata dd.MM.yyyy., splita ga i konvertira na
   * kraj dana i dodaje sat vremena jer je autorova vremenska zona +2.
   *
   * @param vrijeme - varijabla vremena u obliku dd.MM.yyyy.
   * @return long podatak unix vremena
   */
  private long konvertirajDo(String vrijeme) {
    String[] odsjeceno = vrijeme.split("\\.");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date date;
    try {
      date = sdf.parse(odsjeceno[0] + "/" + odsjeceno[1] + "/" + odsjeceno[2]);
    } catch (java.text.ParseException e) {
      return 0;
    }
    long epoch = date.getTime();
    long time = (epoch / 1000) + 26 * 3600;
    return time;
  }

}
