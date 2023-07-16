package org.foi.nwtis.ilucic.aplikacija_2.rest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.LetAviona;
import org.foi.nwtis.rest.podaci.LetAvionaID;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestLetovi koja služi za komuniciranje s openskynetworkom i bazom podataka kada dohvaća
 * spremljene aerodrome.
 *
 * @author Ivan Lucić
 */
@Path("letovi")
@RequestScoped
public class RestLetovi {

  /** Resource inject, služi injektiranju data source-a. */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Kontekst za konfiguracijsku datoteku kako bi se dohvatilo korisničko ime i lozinka od
   * openskynetworka
   */
  @Context
  ServletContext context;

  /**
   * Metoda dajPolaskeLetova uzima icao i query parametar dan koji konvertira u unix vrijeme i
   * dohvaća polaske s tog aerodroma.
   *
   * @param icao - varijabla s kojeg aerodroma se uzimaju polasci.
   * @param dan - dd.MM.yyyy. dana s kojeg se uzimaju polasci.
   * @return Odgovor u json formatu liste klase LetAviona.
   */
  @Path("{icao}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajPolaskeLetova(@PathParam("icao") String icao, @QueryParam("dan") String dan) {
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");

    String korisnik = konfig.dajPostavku("OpenSkyNetwork.korisnik");
    String lozinka = konfig.dajPostavku("OpenSkyNetwork.lozinka");
    long odVremena = konvertirajOd(dan);
    long doVremena = konvertirajDo(dan);
    OSKlijent oSKlijent = new OSKlijent(korisnik, lozinka);

    List<LetAviona> avioniPolasci = null;
    try {
      avioniPolasci = oSKlijent.getDepartures(icao, odVremena, doVremena);
      if (avioniPolasci != null) {
        System.out.println("Broj letova: " + avioniPolasci.size());
      }
    } catch (NwtisRestIznimka e) {
      e.printStackTrace();
    }

    var gson = new Gson();
    var jsonLetovi = gson.toJson(avioniPolasci);
    var odgovor = Response.ok().entity(jsonLetovi).build();

    return odgovor;
  }

  /**
   * Metoda dajLetoveIzmeduAerodroma uzima icaoOd, icaoFrom parametre i query parametar dan koji
   * konvertira u unix vrijeme i dohvaća letove između tih aerodroma.
   *
   * @param icaoFrom the icao from
   * @param icaoTo the icao to
   * @param dan the dan
   * @return the response
   */
  @Path("{icaoOd}/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajLetoveIzmeduAerodroma(@PathParam("icaoOd") String icaoFrom,
      @PathParam("icaoDo") String icaoTo, @QueryParam("dan") String dan) {
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");

    String korisnik = konfig.dajPostavku("OpenSkyNetwork.korisnik");
    String lozinka = konfig.dajPostavku("OpenSkyNetwork.lozinka");
    long odVremena = konvertirajOd(dan);
    long doVremena = konvertirajDo(dan);
    OSKlijent oSKlijent = new OSKlijent(korisnik, lozinka);

    List<LetAviona> avioniPolasci = null;
    List<LetAviona> avioniUkupno = new ArrayList<>();
    try {
      avioniPolasci = oSKlijent.getDepartures(icaoFrom, odVremena, doVremena);
      if (avioniPolasci != null) {
        // System.out.println("Broj letova: " + avioniPolasci.size());
      }
      for (LetAviona a : avioniPolasci) {
        if (a.getEstArrivalAirport() != null) {
          if (a.getEstArrivalAirport().equals("LOWW"))
            avioniUkupno.add(a);
        }
      }
    } catch (NwtisRestIznimka e) {
      e.printStackTrace();
    }

    var gson = new Gson();
    var jsonLetovi = gson.toJson(avioniUkupno);
    var odgovor = Response.ok().entity(jsonLetovi).build();

    return odgovor;
  }

  /**
   * Metoda dajSpremljene dohvaća spremljene podatke iz baze podataka.
   *
   * @return Odgovor u obliku json podataka liste LetAvionaID.
   */
  @Path("spremljeni")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSpremljene() {
    List<LetAvionaID> avioniPolasci = new ArrayList<>();

    String query = "SELECT * FROM LETOVI_POLASCI";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("ID");
        String icao = rs.getString("ICAO24");
        int firstseen = rs.getInt("FIRSTSEEN");
        String estDepart = rs.getString("ESTDEPARTUREAIRPORT");
        int lastseen = rs.getInt("LASTSEEN");
        String estArrival = rs.getString("ESTARRIVALAIRPORT");
        String callsign = rs.getString("CALLSIGN");
        int estDepartureAirportHorizDistance = rs.getInt("ESTDEPARTUREAIRPORTHORIZDISTANCE");
        int estDepartureAirportVertDistance = rs.getInt("ESTDEPARTUREAIRPORTVERTDISTANCE");
        int estArrivalAirportHorizDistance = rs.getInt("ESTARRIVALAIRPORTHORIZDISTANCE");
        int estArrivalAirportVertDistance = rs.getInt("ESTARRIVALAIRPORTVERTDISTANCE");
        int departureAirports = rs.getInt("DEPARTUREAIRPORTCANDIDATESCOUNT");
        int arrivalAirports = rs.getInt("ARRIVALAIRPORTCANDIDATESCOUNT");
        var l = new LetAvionaID(id, icao, firstseen, estDepart, lastseen, estArrival, callsign,
            estDepartureAirportHorizDistance, estDepartureAirportVertDistance,
            estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirports,
            arrivalAirports);
        avioniPolasci.add(l);
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }
    }

    var gson = new Gson();
    var jsonAerodromi = gson.toJson(avioniPolasci);
    var odgovor = Response.ok().entity(jsonAerodromi).build();

    return odgovor;
  }

  /**
   * Metoda konvertirajOd konvertira string vremena formata dd.MM.yyyy., splita ga i konvertira na
   * početak dana i dodaje sat vremena jer je autorova vremenska zona +1.
   *
   * @param vrijeme - varijabla vremena u obliku dd.MM.yyyy.
   * @return long podatak unix vremena
   */
  public long konvertirajOd(String vrijeme) {
    String[] odsjeceno = vrijeme.split("\\.");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date date;
    try {
      date = sdf.parse(odsjeceno[0] + "/" + odsjeceno[1] + "/" + odsjeceno[2]);
    } catch (java.text.ParseException e) {
      return 0;
    }
    long epoch = date.getTime();
    long time = (epoch / 1000) + 3600;
    return time;
  }

  /**
   * Metoda konvertirajOd konvertira string vremena formata dd.MM.yyyy., splita ga i konvertira na
   * kraj dana i dodaje sat vremena jer je autorova vremenska zona +1.
   *
   * @param vrijeme - varijabla vremena u obliku dd.MM.yyyy.
   * @return long podatak unix vremena
   */
  public long konvertirajDo(String vrijeme) {
    String[] odsjeceno = vrijeme.split("\\.");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date date;
    try {
      date = sdf.parse(odsjeceno[0] + "/" + odsjeceno[1] + "/" + odsjeceno[2]);
    } catch (java.text.ParseException e) {
      return 0;
    }
    long epoch = date.getTime();
    long time = (epoch / 1000) + 25 * 3600;
    return time;
  }

}
