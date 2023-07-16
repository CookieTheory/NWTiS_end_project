package org.foi.nwtis.ilucic.aplikacija_5.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.FilterKlasa;
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
public class RestKlijentDnevnika {

  /** Static klasa konfig za url baze */
  private static Konfiguracija konfig;

  /**
   * Kontstruktor RestKlijentAerodroma za popunjavanje konfiga.
   *
   * @param context - uzima kontekst da popuni konfig
   */
  public RestKlijentDnevnika(ServletContext context) {
    konfig = (Konfiguracija) context.getAttribute("konfig");
  }

  public List<FilterKlasa> getDnevnikZapisi(int odBroja, int broj, String vrsta) {
    RestKKlijent rc = new RestKKlijent();
    FilterKlasa[] json_Zapisi = rc.getDnevnikZapisi(odBroja, broj, vrsta);
    List<FilterKlasa> zapisi;
    if (json_Zapisi == null) {
      zapisi = new ArrayList<>();
    } else {
      zapisi = Arrays.asList(json_Zapisi);
    }
    rc.close();
    return zapisi;
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
      webTarget = client.target(BASE_URI).path("dnevnik");
    }

    public FilterKlasa[] getDnevnikZapisi(int odBroja, int broj, String vrsta)
        throws ClientErrorException {
      WebTarget resource = webTarget;

      resource = resource.queryParam("odBroja", odBroja);
      resource = resource.queryParam("broj", broj);
      resource = resource.queryParam("vrsta", vrsta);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Gson gson = new Gson();
      FilterKlasa[] zapisi = gson.fromJson(request.get(String.class), FilterKlasa[].class);

      return zapisi;
    }

    /**
     * Zatvori klijenta.
     */
    public void close() {
      client.close();
    }
  }

}
