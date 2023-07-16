package org.foi.nwtis.ilucic.aplikacija_4.ws;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.ilucic.aplikacija_4.slusaci.Slusac;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@WebService(serviceName = "meteo")
public class WsMeteo {

  @Context
  ServletContext context;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @WebMethod
  public MeteoPodaci dajMeteo(@WebParam String icao) {
    Konfiguracija konfig = Slusac.getKonfig();
    String api = konfig.dajPostavku("OpenWeatherMap.apikey");

    Client client = ClientBuilder.newClient();

    Aerodrom zaLokaciju = new Aerodrom();

    Response odgovor =
        client.target("http://200.20.0.4:8080/ilucic_aplikacija_2/api/aerodromi/" + icao)
            .request(MediaType.APPLICATION_JSON).get();

    if (odgovor.getStatus() == Response.Status.OK.getStatusCode()) {
      zaLokaciju = odgovor.readEntity(Aerodrom.class);
    } else {
      client.close();
      return null;
    }

    client.close();

    OWMKlijent oWMKlijent = new OWMKlijent(api);
    MeteoPodaci meteoPodaci = new MeteoPodaci();

    try {
      meteoPodaci = oWMKlijent.getRealTimeWeather(zaLokaciju.getLokacija().getLatitude(),
          zaLokaciju.getLokacija().getLongitude());
    } catch (NwtisRestIznimka e) {
      e.printStackTrace();
    }

    return meteoPodaci;
  }

}
