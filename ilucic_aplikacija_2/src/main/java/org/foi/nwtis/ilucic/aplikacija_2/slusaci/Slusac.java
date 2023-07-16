package org.foi.nwtis.ilucic.aplikacija_2.slusaci;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Klasa Slusac koja služi kao listener za aplikaciju wa_1, za aplikaciju wa_2 vidjeti Slusac2.
 *
 * @author Ivan Lucić
 */
public class Slusac implements ServletContextListener {

  /** Kontekst iz kojeg ze uzima konfiguracija */
  private ServletContext kontekst = null;

  /**
   * Kontekst se incijalizira u ovoj metodi
   *
   * @param event - kada se pokreće aplikacija (u payari nakon deploya se odmah launcha)
   */
  public void contextInitialized(ServletContextEvent event) {
    this.kontekst = event.getServletContext();
    ucitajKonfiguraciju();
  }

  /**
   * Metoda koja učitava konfiguraciju i stavlja je u atribut "konfiguracija", koristi apstraktnu
   * klasu konfiguracija s početka vježbi koja učitava s bilo kojeg formata.
   */
  private void ucitajKonfiguraciju() {
    Konfiguracija konfig = null;
    String path = this.kontekst.getRealPath("/WEB-INF") + java.io.File.separator;
    String datoteka = this.kontekst.getInitParameter("konfiguracija");
    try {
      konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(path + datoteka);
      this.kontekst.setAttribute("konfig", konfig);
      Socket provjeraAplikacije1 = provjeraAplikacije1(konfig.dajPostavku("aplikacija_1.adresa"),
          Integer.parseInt(konfig.dajPostavku("aplikacija_1.vrata")));
      if (provjeraAplikacije1 == null) {
        System.out.println("Aplikacija 1 ne radi, gasim aplikaciju 2.");
        Thread.currentThread().interrupt();
        System.exit(0);
      }
      provjeraAplikacije1.close();
    } catch (Exception e) {
      Logger.getLogger(Slusac.class.getName()).log(Level.SEVERE,
          "Problem s ucitavanjem konfiguracije: " + path + datoteka);
    }
  }

  /**
   * Metoda u kojoj se kontekst uništava
   *
   * @param event - event koji pokreće uništavanje (u payari nakon undeployanja/moguće i
   *        zaustavljanja)
   */
  public void contextDestroyed(ServletContextEvent event) {
    this.kontekst = event.getServletContext();
    System.out.println("Obrisan kontekst: " + this.kontekst.getServletContextName());
  }

  private Socket provjeraAplikacije1(String adresa, int vrata) {
    Socket provjereno = null;
    try {
      provjereno = new Socket(adresa, vrata);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return provjereno;
  }

}
