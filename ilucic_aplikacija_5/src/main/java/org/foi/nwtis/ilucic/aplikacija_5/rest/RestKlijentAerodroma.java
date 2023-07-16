package org.foi.nwtis.ilucic.aplikacija_5.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodromDrzava;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 * Klasa RestKlijentAerodroma koja vraća podatke o aerodromima i udaljenostima.
 *
 * @author Ivan Lucić
 */
public class RestKlijentAerodroma {

  /** Static klasa konfig za url baze */
  private static Konfiguracija konfig;

  /**
   * Kontstruktor RestKlijentAerodroma za popunjavanje konfiga.
   *
   * @param context - uzima kontekst da popuni konfig
   */
  public RestKlijentAerodroma(ServletContext context) {
    konfig = (Konfiguracija) context.getAttribute("konfig");
  }

  /**
   * Metoda getAerodromi dohvaća sve aerodrome s parametrima za straničenje odBroja, broj.
   *
   * @param odBroja - od kojeg reda.
   * @param broj - do kojeg reda.
   * @return Vraća listu Aerodroma.
   */
  public List<Aerodrom> getAerodromi(int odBroja, int broj, String traziNaziv, String traziDrzavu) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom[] json_Aerodromi = rc.getAerodromi(odBroja, broj, traziNaziv, traziDrzavu);
    List<Aerodrom> aerodromi;
    if (json_Aerodromi == null) {
      aerodromi = new ArrayList<>();
    } else {
      aerodromi = Arrays.asList(json_Aerodromi);
    }
    rc.close();
    return aerodromi;
  }

  /**
   * Metoda getAerodrom dohvaća jedan aerodrom.
   *
   * @param icao - icao aerodroma
   * @return Vraća objekt Aerodrom od icao.
   */
  public Aerodrom getAerodrom(String icao) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom k = rc.getAerodrom(icao);
    rc.close();
    return k;
  }

  /**
   * Metoda get2AerddromaUdaljenosti dohvaća udaljenosti između dva aerdroma.
   *
   * @param icaoOd - od kojeg aerodroma.
   * @param icaoDo - do kojeg aerodroma
   * @return Listu Udaljenosti za aerodrome.
   */
  public List<Udaljenost> get2AerodromaUdaljenosti(String icaoOd, String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    Udaljenost[] json_Udaljenosti = rc.get2AerodromaUdaljenosti(icaoOd, icaoDo);
    List<Udaljenost> udaljenosti;
    if (json_Udaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(json_Udaljenosti);
    }
    rc.close();
    return udaljenosti;
  }

  public String get2AerodromaIzracunaj(String icaoOd, String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    String json_Izracunaj = rc.get2AerodromaIzracunaj(icaoOd, icaoDo);
    String udaljenost;
    if (json_Izracunaj == null) {
      udaljenost = "";
    } else {
      udaljenost = json_Izracunaj;
    }
    rc.close();
    return udaljenost;
  }

  public List<UdaljenostAerodromDrzava> get2AerodromaUdaljenost1(String icaoOd, String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostAerodromDrzava[] json_Udaljenosti = rc.get2AerodromaUdaljenost1(icaoOd, icaoDo);
    List<UdaljenostAerodromDrzava> udaljenosti;
    if (json_Udaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(json_Udaljenosti);
    }
    rc.close();
    return udaljenosti;
  }

  public List<UdaljenostAerodromDrzava> getAerodromUdaljenosti2(String icaoOd, String drzava,
      float km) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostAerodromDrzava[] json_Udaljenosti = rc.getAerodromUdaljenosti2(icaoOd, drzava, km);
    List<UdaljenostAerodromDrzava> udaljenosti;
    if (json_Udaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(json_Udaljenosti);
    }
    rc.close();
    return udaljenosti;
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
    private static String BASE_URI = "http://200.20.0.4:8080/ilucic_zadaca_2_wa_1/api";

    /**
     * Instancira novi RestKKlijent i postavlja putanju do prve aplikacije za aerodrome.
     */
    public RestKKlijent() {
      String adresa = konfig.dajPostavku("adresa.aplikacija_2");
      BASE_URI = adresa;
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("aerodromi");
    }

    /**
     * Metoda getAerodromi dohvaća sve aerodrome s parametrima za straničenje odBroja, broj.
     *
     * @param odBroja - od kojeg reda.
     * @param broj - do kojeg reda.
     * @return Vraća listu Aerodroma.
     * @throws ClientErrorException u slučaju greške s requestom.
     */
    public Aerodrom[] getAerodromi(int odBroja, int broj, String traziNaziv, String traziDrzavu)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.queryParam("odBroja", odBroja);
      resource = resource.queryParam("broj", broj);
      if (traziNaziv != null && !traziNaziv.isEmpty()) {
        resource = resource.queryParam("traziNaziv", traziNaziv);
      }
      if (traziDrzavu != null && !traziDrzavu.isEmpty()) {
        resource = resource.queryParam("traziDrzavu", traziDrzavu);
      }
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom[] aerodromi = gson.fromJson(request.get(String.class), Aerodrom[].class);

      return aerodromi;
    }

    /**
     * Metoda getAerodrom dohvaća jedan aerodrom.
     *
     * @param icao - icao aerodroma
     * @return Vraća objekt Aerodrom od icao.
     * @throws ClientErrorException u slučaju greške s requestom.
     */
    public Aerodrom getAerodrom(String icao) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom aerodrom = gson.fromJson(request.get(String.class), Aerodrom.class);
      return aerodrom;
    }

    /**
     * Metoda get2AerddromaUdaljenosti dohvaća udaljenosti između dva aerdroma.
     *
     * @param icaoOd - od kojeg aerodroma.
     * @param icaoDo - do kojeg aerodroma
     * @return Listu Udaljenosti za aerodrome.
     * @throws ClientErrorException u slučaju greške s requestom.
     */
    public Udaljenost[] get2AerodromaUdaljenosti(String icaoOd, String icaoDo)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      resource =
          resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[] {icaoOd, icaoDo}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Udaljenost[] udaljenosti = gson.fromJson(request.get(String.class), Udaljenost[].class);
      return udaljenosti;
    }

    public String get2AerodromaIzracunaj(String icaoOd, String icaoDo) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource
          .path(java.text.MessageFormat.format("{0}/izracunaj/{1}", new Object[] {icaoOd, icaoDo}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Gson gson = new Gson();
      String udaljenost = gson.fromJson(request.get(String.class), String.class);
      return udaljenost;
    }

    public UdaljenostAerodromDrzava[] get2AerodromaUdaljenost1(String icaoOd, String icaoDo)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.path(
          java.text.MessageFormat.format("{0}/udaljenost1/{1}", new Object[] {icaoOd, icaoDo}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Gson gson = new Gson();
      UdaljenostAerodromDrzava[] udaljenosti =
          gson.fromJson(request.get(String.class), UdaljenostAerodromDrzava[].class);
      return udaljenosti;
    }

    public UdaljenostAerodromDrzava[] getAerodromUdaljenosti2(String icaoOd, String drzava,
        float km) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource =
          resource.path(java.text.MessageFormat.format("{0}/udaljenost2", new Object[] {icaoOd}));
      resource = resource.queryParam("drzava", drzava);
      resource = resource.queryParam("km", km);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Gson gson = new Gson();
      UdaljenostAerodromDrzava[] udaljenosti =
          gson.fromJson(request.get(String.class), UdaljenostAerodromDrzava[].class);
      return udaljenosti;
    }

    /**
     * Zatvori klijenta.
     */
    public void close() {
      client.close();
    }
  }

}
