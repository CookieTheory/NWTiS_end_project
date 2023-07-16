package org.foi.nwtis.ilucic.aplikacija_4.ws;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.AerodromiLetovi;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.Airports;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.Korisnici;
import org.foi.nwtis.ilucic.aplikacija_4.zrna.AerodromiLetoviFacade;
import org.foi.nwtis.ilucic.aplikacija_4.zrna.AirportFacade;
import org.foi.nwtis.ilucic.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.ilucic.aplikacija_4.zrna.LetoviPolasciFacade;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.PogresnaAutentikacija;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "aerodromi")
public class WsAerodromi {

  @Inject
  AirportFacade airportFacade;

  @Inject
  KorisniciFacade korisniciFacade;

  @Inject
  LetoviPolasciFacade letoviPolasciFacade;

  @Inject
  AerodromiLetoviFacade aerodromiLetoviFacade;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @WebMethod
  public List<Aerodrom> dajAerodromeZaLetove(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka) {
    Korisnici provjera;
    try {
      provjera = korisniciFacade.pronadiKorisnikaZaLoginIliProvjeru(korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      System.out.println(e.getMessage());
      return null;
    }
    if (provjera != null) {
      List<Aerodrom> aerodromi = new ArrayList<>();
      List<Airports> avioni = aerodromiLetoviFacade.dajAerodromeZaLetove();
      for (Airports a : avioni) {
        var koord = a.getCoordinates().split(",");
        var lokacija = new Lokacija(koord[1], koord[0]);
        Aerodrom aerodrom = new Aerodrom(a.getIcao(), a.getName(), a.getIsoCountry(), lokacija);
        aerodromi.add(aerodrom);
      }
      return aerodromi;
    } else {
      return null;
    }
  }

  @WebMethod
  public boolean dodajAerodromZaLetove(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao) {
    Korisnici provjera;
    try {
      provjera = korisniciFacade.pronadiKorisnikaZaLoginIliProvjeru(korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      System.out.println(e.getMessage());
      return false;
    }
    if (provjera != null) {
      Airports airport = airportFacade.find(icao);
      if (airport != null) {
        AerodromiLetovi aerodromiLetovi = new AerodromiLetovi();
        aerodromiLetovi.setAirport(airport);
        aerodromiLetovi.setStatus(true);
        aerodromiLetoviFacade.create(aerodromiLetovi);
        System.out.println("Dodem do ovdje!");
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @WebMethod
  public boolean pauzirajAerodromZaLetove(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao) {
    Korisnici provjera;
    try {
      provjera = korisniciFacade.pronadiKorisnikaZaLoginIliProvjeru(korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      System.out.println(e.getMessage());
      return false;
    }
    if (provjera != null) {
      AerodromiLetovi aerodrom = aerodromiLetoviFacade.dajAerodromLetPoIcao(icao);
      if (aerodrom != null) {
        aerodrom.setStatus(false);
        aerodromiLetoviFacade.edit(aerodrom);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @WebMethod
  public boolean aktivirajAerodromZaLetove(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao) {
    Korisnici provjera;
    try {
      provjera = korisniciFacade.pronadiKorisnikaZaLoginIliProvjeru(korisnik, lozinka);
    } catch (PogresnaAutentikacija e) {
      System.out.println(e.getMessage());
      return false;
    }
    if (provjera != null) {
      AerodromiLetovi aerodrom = aerodromiLetoviFacade.dajAerodromLetPoIcao(icao);
      if (aerodrom != null) {
        aerodrom.setStatus(true);
        aerodromiLetoviFacade.edit(aerodrom);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

}
