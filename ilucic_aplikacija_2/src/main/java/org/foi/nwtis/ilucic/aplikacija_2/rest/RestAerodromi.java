package org.foi.nwtis.ilucic.aplikacija_2.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.podaci.UdaljenostAerodromDrzava;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestAerodromi, služi za komunikaciju s docker bazom podataka u dohvaćanju podataka oko
 * aerodroma i udaljenosti.
 *
 * @author Ivan Lucić
 */
@Path("aerodromi")
@RequestScoped
public class RestAerodromi {

  /** Resource inject, služi injektiranju data source-a. */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Context
  ServletContext context;

  /**
   * Metoda dajSveAerodrome. Služi za dohvaćanje svih aerodroma iz baze podataka. Dohvaća ICAO,
   * NAME, ICO_COUNTRY, COORDINATES iz AIRPORTS tablice. Također sadrži OFFSET i LIMIT za frontend
   * straničenje podataka. Povezuje se na bazu s ds.getConnection();
   *
   * @param odBroja - varijabla koja služi od kojeg podatka se izvlači (stranjičenje)
   * @param broj - varijabla koja služi do kojeg podatka se izvlači (straničenje)
   * @return odgovor u obliku json lista formatiranih podataka koji sadrže klasu Aerodrom.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome(@QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj, @QueryParam("traziNaziv") String traziNaziv,
      @QueryParam("traziDrzavu") String traziDrzavu) {
    int brojParametra = 2;
    String query = "SELECT * FROM AIRPORTS";

    if (traziNaziv != null) {
      query += " WHERE NAME LIKE ?";
      traziNaziv = "%" + traziNaziv + "%";
      brojParametra++;
      if (traziDrzavu != null) {
        query += " AND ISO_COUNTRY = ?";
        brojParametra++;
      }
    } else if (traziDrzavu != null) {
      query += " WHERE ISO_COUNTRY = ?";
      brojParametra++;
    }

    query += " OFFSET ? LIMIT ?";

    List<Aerodrom> aerodromi = dajSveAerodromeKomunikacijaSBazom(odBroja, broj, traziNaziv,
        traziDrzavu, brojParametra, query);

    var gson = new Gson();
    var jsonAerodromi = gson.toJson(aerodromi);
    var odgovor = Response.ok().entity(jsonAerodromi).build();

    return odgovor;
  }

  private List<Aerodrom> dajSveAerodromeKomunikacijaSBazom(int odBroja, int broj, String traziNaziv,
      String traziDrzavu, int brojParametra, String query) {
    List<Aerodrom> aerodromi = new ArrayList<>();

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      prepareStatementZaSveAerodrome(odBroja, broj, traziNaziv, traziDrzavu, brojParametra, stmt);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String icao = rs.getString("ICAO");
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String koordinate = rs.getString("COORDINATES");
        String[] lokacija = koordinate.split(",");
        Lokacija aerodromLokacija = new Lokacija(lokacija[0], lokacija[1]);
        var a = new Aerodrom(icao, naziv, drzava, aerodromLokacija);
        aerodromi.add(a);
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
    return aerodromi;
  }

  private void prepareStatementZaSveAerodrome(int odBroja, int broj, String traziNaziv,
      String traziDrzavu, int brojParametra, PreparedStatement stmt) throws SQLException {
    for (int i = 1; i < brojParametra; i++) {
      if (traziNaziv != null) {
        stmt.setString(i, traziNaziv);
        i++;
        if (traziDrzavu != null) {
          stmt.setString(i, traziDrzavu);
          i++;
        }
      } else if (traziDrzavu != null) {
        stmt.setString(i, traziDrzavu);
        i++;
      }
      stmt.setInt(i, odBroja - 1);
      i++;
      stmt.setInt(i, broj);
    }
  }

  /**
   * Metoda dajAerodrom, služi za dohvaćanje samo jednog aerodroma iz baze podataka. Query dohvaća
   * ICAO, NAME, ISO_COUNTRY, COORDINATES iz tablice AIRPORTS. Povezuje se na bazu s
   * ds.getConnection();
   *
   * @param icao - icao parametar koji služi za određivanje koji aerodrom dohvaćamo.
   * @return Odgovor u obliku json formata samo jedne klase Aerodrom.
   */
  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodrom(@PathParam("icao") String icao) {
    Aerodrom aerodrom = null;

    String query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ICAO = ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String icaodohvaceni = rs.getString("ICAO");
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String koordinate = rs.getString("COORDINATES");
        String[] lokacija = koordinate.split(",");
        Lokacija aerodromLokacija = new Lokacija(lokacija[1], lokacija[0]);
        var a = new Aerodrom(icaodohvaceni, naziv, drzava, aerodromLokacija);
        aerodrom = a;
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

    if (aerodrom == null) {
      return Response.status(404).build();
    } else {
      var gson = new Gson();
      var jsonAerodromi = gson.toJson(aerodrom);
      var odgovor = Response.ok().entity(jsonAerodromi).build();

      return odgovor;
    }
  }

  /**
   * Metoda dajUdaljenostiIzmeduDvaAerodroma dohvaća iz baze podataka udaljenost između dva
   * aerodroma po distanci države. Dohvaća ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY iz matrice
   * udaljenosti. Povezuje se na bazu s ds.getConnection();
   *
   * @param icaoFrom - varijabla koja sluzi za određivanje od kojeg aerodroma se uzima udaljenost
   * @param icaoTo - varijabla koja služi za određivanje do kojeg aerodroma se uzima udaljenost
   * @return Odgovor u obliku json formatirane liste udaljenosti (Udaljenost)
   */
  @Path("{icaoOd}/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeduDvaAerodoma(@PathParam("icaoOd") String icaoFrom,
      @PathParam("icaoDo") String icaoTo) {

    var udaljenosti = new ArrayList<Udaljenost>();

    String query =
        "SELECT ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ? AND ICAO_TO = ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icaoFrom);
      stmt.setString(2, icaoTo);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String drzava = rs.getString("COUNTRY");
        float udaljenost = rs.getFloat("DIST_CTRY");
        var u = new Udaljenost(drzava, udaljenost);
        udaljenosti.add(u);
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
    var jsonAerodromi = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonAerodromi).build();

    return odgovor;
  }

  /**
   * Metoda dajUdaljenostiIzmeduSvihAerodroma uzima jedan ICAO te iz njega izvlači sve udaljenosti
   * od tog aerodroma. Također ima straničenje i tome služi OFFSET i LIMIT za frontend. Izvlači
   * DISTINCT ICAO_TO, DIST_TOT kako se ne bi ponavljali elementi. Povezuje se na bazu s
   * ds.getConnection();
   *
   * @param icaoFrom - varijabla icao koja služi od kojeg se aerodroma uzimaju udaljenosti.
   * @param odBroja - varijabla koja služi od kojeg podatka se izvlači (stranjičenje)
   * @param broj - varijabla koja služi do kojeg podatka se izvlači (straničenje)
   * @return odgovor u obliku json formatiranih podataka koji sadrže listu klasa UdaljenostAerodrom.
   */
  @Path("{icao}/udaljenosti")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeduSvihAerodoma(@PathParam("icao") String icaoFrom,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    List<UdaljenostAerodrom> udaljenosti = new ArrayList<>();

    String query =
        "SELECT DISTINCT ICAO_TO, DIST_TOT FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ? OFFSET ? LIMIT ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icaoFrom);
      stmt.setInt(2, odBroja - 1);
      stmt.setInt(3, broj);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String icao = rs.getString("ICAO_TO");
        float km = rs.getFloat("DIST_TOT");
        var u = new UdaljenostAerodrom(icao, km);
        udaljenosti.add(u);
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
    var jsonAerodromi = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonAerodromi).build();

    return odgovor;
  }

  @Path("{icaoOd}/izracunaj/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajIzracunaj2Aerodroma(@PathParam("icaoOd") String icaoFrom,
      @PathParam("icaoDo") String icaoTo) {
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    String adresa = konfig.dajPostavku("aplikacija_1.adresa");
    int vrata = Integer.parseInt(konfig.dajPostavku("aplikacija_1.vrata"));
    List<Aerodrom> aerodromi = new ArrayList<>();

    String query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ICAO = ?";

    dohvatiListuAerodroma(aerodromi, query, icaoFrom);
    dohvatiListuAerodroma(aerodromi, query, icaoTo);

    String udaljenost = "UDALJENOST " + aerodromi.get(0).getLokacija().getLatitude().trim() + " "
        + aerodromi.get(0).getLokacija().getLongitude().trim() + " "
        + aerodromi.get(1).getLokacija().getLatitude().trim() + " "
        + aerodromi.get(1).getLokacija().getLongitude().trim();

    Socket uticnica = povezi(adresa, vrata);
    String poruka = posaljiPoruku(udaljenost, uticnica);
    String[] izracunataUdaljenost = poruka.split(" ");
    if (izracunataUdaljenost[0] == "Error") {
      izracunataUdaljenost[1] = poruka;
    }


    var gson = new Gson();
    var jsonAerodromi = gson.toJson(izracunataUdaljenost[1]);
    var odgovor = Response.ok().entity(jsonAerodromi).build();

    return odgovor;
  }

  private void dohvatiListuAerodroma(List<Aerodrom> aerodromi, String query, String icao) {
    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String icaodohvaceni = rs.getString("ICAO");
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String koordinate = rs.getString("COORDINATES");
        String[] lokacija = koordinate.split(",");
        Lokacija aerodromLokacija = new Lokacija(lokacija[1], lokacija[0]);
        var a = new Aerodrom(icaodohvaceni, naziv, drzava, aerodromLokacija);
        aerodromi.add(a);
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
  }

  @Path("{icaoOd}/udaljenost1/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajIzracunaj2AerodromaUdaljenost(@PathParam("icaoOd") String icaoFrom,
      @PathParam("icaoDo") String icaoTo) {
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    String adresa = konfig.dajPostavku("aplikacija_1.adresa");
    int vrata = Integer.parseInt(konfig.dajPostavku("aplikacija_1.vrata"));
    List<Aerodrom> aerodromi = new ArrayList<>();
    List<Aerodrom> aerodromiZaIzracun = new ArrayList<>();
    List<UdaljenostAerodromDrzava> konacneUdaljenosti = new ArrayList<>();

    String query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ICAO = ?";

    dohvatiListuAerodroma(aerodromi, query, icaoFrom);
    dohvatiListuAerodroma(aerodromi, query, icaoTo);

    Float mjerenje = izracunajUdaljenost(adresa, vrata, aerodromi.get(0), aerodromi.get(1));

    query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ISO_COUNTRY = ?";
    dohvatiListuAerodroma(aerodromiZaIzracun, query, aerodromi.get(1).getDrzava());

    for (Aerodrom aerodrom : aerodromiZaIzracun) {
      Float mjerenjeIzmeduPosebnih = izracunajUdaljenost(adresa, vrata, aerodromi.get(0), aerodrom);

      if (mjerenjeIzmeduPosebnih != 05 && mjerenjeIzmeduPosebnih < mjerenje) {
        konacneUdaljenosti.add(new UdaljenostAerodromDrzava(aerodrom.getIcao(),
            aerodrom.getDrzava(), mjerenjeIzmeduPosebnih));
      }
    }

    var gson = new Gson();
    var jsonAerodromi = gson.toJson(konacneUdaljenosti);
    var odgovor = Response.ok().entity(jsonAerodromi).build();

    return odgovor;
  }

  @Path("{icaoOd}/udaljenost2")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajIzracunajUdaljenost2(@PathParam("icaoOd") String icaoFrom,
      @QueryParam("drzava") String drzava, @QueryParam("km") float km) {
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    String adresa = konfig.dajPostavku("aplikacija_1.adresa");
    int vrata = Integer.parseInt(konfig.dajPostavku("aplikacija_1.vrata"));
    List<Aerodrom> aerodromi = new ArrayList<>();
    List<Aerodrom> aerodromiZaIzracun = new ArrayList<>();
    List<UdaljenostAerodromDrzava> konacneUdaljenosti = new ArrayList<>();

    String query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ICAO = ?";

    dohvatiListuAerodroma(aerodromi, query, icaoFrom);

    query = "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ISO_COUNTRY = ?";
    dohvatiListuAerodroma(aerodromiZaIzracun, query, drzava);

    for (Aerodrom aerodrom : aerodromiZaIzracun) {
      Float mjerenjeIzmeduPosebnih = izracunajUdaljenost(adresa, vrata, aerodromi.get(0), aerodrom);

      if (mjerenjeIzmeduPosebnih != 05 && mjerenjeIzmeduPosebnih < km) {
        konacneUdaljenosti.add(new UdaljenostAerodromDrzava(aerodrom.getIcao(),
            aerodrom.getDrzava(), mjerenjeIzmeduPosebnih));
      }
    }

    var gson = new Gson();
    var jsonAerodromi = gson.toJson(konacneUdaljenosti);
    var odgovor = Response.ok().entity(jsonAerodromi).build();

    return odgovor;
  }

  private Float izracunajUdaljenost(String adresa, int vrata, Aerodrom aerodrom1,
      Aerodrom aerodrom2) {
    String udaljenost = "UDALJENOST " + aerodrom1.getLokacija().getLatitude().trim() + " "
        + aerodrom1.getLokacija().getLongitude().trim() + " "
        + aerodrom2.getLokacija().getLatitude().trim() + " "
        + aerodrom2.getLokacija().getLongitude().trim();

    Socket uticnica = povezi(adresa, vrata);
    String poruka = posaljiPoruku(udaljenost, uticnica);
    String[] izracunataUdaljenost = poruka.split(" ");

    return Float.parseFloat(izracunataUdaljenost[1]);
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
