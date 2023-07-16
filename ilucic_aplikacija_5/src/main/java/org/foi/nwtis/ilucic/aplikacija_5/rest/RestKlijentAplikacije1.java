package org.foi.nwtis.ilucic.aplikacija_5.rest;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.NadzorKlasa;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestKlijentAerodroma koja vraća podatke o aerodromima i udaljenostima.
 *
 * @author Ivan Lucić
 */
public class RestKlijentAplikacije1 {

  /** Static klasa konfig za url baze */
  private static Konfiguracija konfig;

  /**
   * Kontstruktor RestKlijentAerodroma za popunjavanje konfiga.
   *
   * @param context - uzima kontekst da popuni konfig
   */
  public RestKlijentAplikacije1(ServletContext context) {
    konfig = (Konfiguracija) context.getAttribute("konfig");
  }

  /**
   * Metoda getAerodromi dohvaća sve aerodrome s parametrima za straničenje odBroja, broj.
   *
   * @param odBroja - od kojeg reda.
   * @param broj - do kojeg reda.
   * @return Vraća listu Aerodroma.
   */
  public String getStatus() {
    RestKKlijent rc = new RestKKlijent();
    String odgovor = rc.getStatus();
    rc.close();
    return odgovor;
  }

  /**
   * Metoda getAerodrom dohvaća jedan aerodrom.
   *
   * @param icao - icao aerodroma
   * @return Vraća objekt Aerodrom od icao.
   */
  public String getKomanda(String komanda) {
    RestKKlijent rc = new RestKKlijent();
    String odgovor = rc.getKomanda(komanda);
    rc.close();
    return odgovor;
  }

  public String getInfo(String INFO, String komanda) {
    RestKKlijent rc = new RestKKlijent();
    String odgovor = rc.getInfo(INFO, komanda);
    rc.close();
    return odgovor;
  }

  /**
   * Statična klasa RestKKlijent koja se bavi s komunikacijom s prvom aplikacijom.
   *
   * @author Ivan Lucić
   */
  static class RestKKlijent {

    /** WebTarget */
    private final WebTarget webTarget;

    /** Klijent */
    private final Client client;

    /** URL prve aplikacije */
    private static String BASE_URI = "http://200.20.0.4:8080/ilucic_aplikacija_2/api";

    /**
     * Instancira novi RestKKlijent i postavlja putanju do prve aplikacije za aerodrome.
     */
    public RestKKlijent() {
      String adresa = konfig.dajPostavku("adresa.aplikacija_2");
      BASE_URI = adresa;
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nadzor");
    }

    public String getStatus() {
      WebTarget resource = webTarget;
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response response = request.get();
      Gson gson = new Gson();

      if (response.getStatus() == Response.Status.OK.getStatusCode()) {
        NadzorKlasa odgovor = gson.fromJson(response.readEntity(String.class), NadzorKlasa.class);
        return odgovor.getOpis();
      } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
        NadzorKlasa odgovor = gson.fromJson(response.readEntity(String.class), NadzorKlasa.class);
        return odgovor.getOpis();
      }
      return null;
    }

    public String getKomanda(String komanda) {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {komanda}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response response = request.get();
      Gson gson = new Gson();

      if (response.getStatus() == Response.Status.OK.getStatusCode()) {
        NadzorKlasa odgovor = gson.fromJson(response.readEntity(String.class), NadzorKlasa.class);
        return odgovor.getOpis();
      } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
        NadzorKlasa odgovor = gson.fromJson(response.readEntity(String.class), NadzorKlasa.class);
        return odgovor.getOpis();
      }
      return null;
    }

    public String getInfo(String INFO, String komanda) {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[] {INFO, komanda}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response response = request.get();
      Gson gson = new Gson();

      if (response.getStatus() == Response.Status.OK.getStatusCode()) {
        NadzorKlasa odgovor = gson.fromJson(response.readEntity(String.class), NadzorKlasa.class);
        return odgovor.getOpis();
      } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
        NadzorKlasa odgovor = gson.fromJson(response.readEntity(String.class), NadzorKlasa.class);
        return odgovor.getOpis();
      }
      return null;
    }

    /**
     * Zatvori klijenta.
     */
    public void close() {
      client.close();
    }
  }

}
