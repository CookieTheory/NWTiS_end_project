package org.foi.nwtis.ilucic.aplikacija_4.slusaci;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Klasa Slusac2 koja služi kao listener za aplikaciju wa_2, za aplikaciju wa_1 vidjeti Slusac.
 *
 * @author Ivan Lucić
 */
@WebListener()
public class Slusac implements ServletContextListener {

  /** Kontekst iz kojeg se preuzima konfiguracija */
  private static Konfiguracija konfig = null;

  /**
   * Kontekst se incijalizira u ovoj metodi
   *
   * @param event - kada se pokreće aplikacija (u payari nakon deploya se odmah launcha)
   */
  @Override
  public void contextInitialized(ServletContextEvent event) {
    ServletContext kontekst = event.getServletContext();
    ucitajKonfiguraciju(kontekst);
  }

  /**
   * Metoda koja učitava konfiguraciju i stavlja je u atribut "konfiguracija", koristi apstraktnu
   * klasu konfiguracija s početka vježbi koja učitava s bilo kojeg formata.
   */
  private void ucitajKonfiguraciju(ServletContext kontekst) {
    String path = kontekst.getRealPath("WEB-INF") + java.io.File.separator;
    String datoteka = path + kontekst.getInitParameter("konfiguracija");
    try {
      konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
      kontekst.setAttribute("konfig", konfig);
      Socket provjeraAplikacije1 = provjeraAplikacije1(konfig.dajPostavku("aplikacija_1.adresa"),
          Integer.parseInt(konfig.dajPostavku("aplikacija_1.vrata")));
      if (provjeraAplikacije1 == null) {
        Logger.getLogger(Slusac.class.getName()).log(Level.SEVERE,
            "Aplikacija 1 ne radi, gasim aplikaciju 4.");
        Thread.currentThread().interrupt();
      } else {
        Logger.getLogger(Slusac.class.getName()).log(Level.INFO,
            "Aplikacija 1 je uspješno kontaktirana.");
      }
      provjeraAplikacije1.close();
      Logger.getLogger(Slusac.class.getName()).log(Level.INFO, "Uspjesno ucitano!" + datoteka);
    } catch (Exception e) {
      Logger.getLogger(Slusac.class.getName()).log(Level.SEVERE,
          "Problem s aplikacijom 1 ili ucitavanjem: " + datoteka);
    }
  }

  /**
   * Metoda u kojoj se kontekst uništava
   *
   * @param event - event koji pokreće uništavanje (u payari nakon undeployanja/moguće i
   *        zaustavljanja)
   */
  @Override
  public void contextDestroyed(ServletContextEvent event) {
    System.out.println("Obrisan kontekst: " + event.getServletContext().getContextPath());
  }

  public static Konfiguracija getKonfig() {
    return konfig;
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
