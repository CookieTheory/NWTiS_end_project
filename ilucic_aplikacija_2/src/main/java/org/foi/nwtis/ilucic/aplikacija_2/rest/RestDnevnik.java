package org.foi.nwtis.ilucic.aplikacija_2.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.FilterKlasa;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestAerodromi, služi za komunikaciju s prvom aplikacijom u dohvaćanju podataka oko stanja
 * itd.
 *
 * @author Ivan Lucić
 */
@Path("dnevnik")
@RequestScoped
public class RestDnevnik {

  /** Resource inject, služi injektiranju data source-a. */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Context
  ServletContext context;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dohvatiDnevnik(@QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj, @QueryParam("vrsta") String vrsta) {
    List<FilterKlasa> dnevnikZapisi = new ArrayList<>();
    int brojParametra = 2;

    String query = "SELECT * FROM DNEVNIK";
    if (vrsta != null) {
      query += " WHERE APLIKACIJA = ?";
      brojParametra++;
    }
    query += " OFFSET ? LIMIT ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = pripremiStatement(odBroja, broj, vrsta, brojParametra, query, con);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String vrstaDnevnika = rs.getString("VRSTA");
        String aplikacija = rs.getString("APLIKACIJA");
        String url = rs.getString("URL");
        Timestamp vrijeme = rs.getTimestamp("VRIJEME");
        var d = new FilterKlasa(vrstaDnevnika, aplikacija, url, vrijeme);
        dnevnikZapisi.add(d);
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
    var jsonZapisi = gson.toJson(dnevnikZapisi);
    var odgovor = Response.ok().entity(jsonZapisi).build();

    return odgovor;
  }

  private PreparedStatement pripremiStatement(int odBroja, int broj, String vrsta,
      int brojParametra, String query, Connection con) throws SQLException {
    PreparedStatement stmt;
    stmt = con.prepareStatement(query);
    for (int i = 1; i < brojParametra; i++) {
      if (vrsta != null) {
        stmt.setString(i, vrsta);
        i++;
      }
      stmt.setInt(i, odBroja - 1);
      i++;
      stmt.setInt(i, broj);
    }
    return stmt;
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response spremiZahtjev(FilterKlasa zapis) {

    String query = "INSERT INTO DNEVNIK (VRSTA, APLIKACIJA, URL, VRIJEME) VALUES (?,?,?,?)";

    int uspjeh = 0;

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, zapis.getVrsta());
      stmt.setString(2, zapis.getAplikacija());
      stmt.setString(3, zapis.getRequestUrl());
      stmt.setTimestamp(4, zapis.getVrijeme());

      int rs = stmt.executeUpdate();
      if (rs != 0)
        uspjeh = 1;
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
    if (uspjeh != 0) {
      return Response.status(201).build();
    } else {
      return Response.status(400).build();
    }
  }

}
