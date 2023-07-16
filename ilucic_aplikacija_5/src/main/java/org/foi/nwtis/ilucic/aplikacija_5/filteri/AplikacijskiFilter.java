package org.foi.nwtis.ilucic.aplikacija_5.filteri;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.foi.nwtis.podaci.FilterKlasa;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

public class AplikacijskiFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpZahtjev = (HttpServletRequest) request;
    String requestUrl = httpZahtjev.getRequestURI();
    LocalDateTime sada = LocalDateTime.now();
    Timestamp timestamp = Timestamp.valueOf(sada);

    Client client = ClientBuilder.newClient();

    FilterKlasa zaSpremanje = new FilterKlasa();
    zaSpremanje.setVrsta("UI");
    zaSpremanje.setAplikacija("AP5");
    zaSpremanje.setRequestUrl(requestUrl);
    zaSpremanje.setVrijeme(timestamp);

    client.target("http://200.20.0.4:8080/ilucic_aplikacija_2/api/dnevnik")
        .request(MediaType.APPLICATION_JSON).post(Entity.json(zaSpremanje));

    client.close();
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

}
