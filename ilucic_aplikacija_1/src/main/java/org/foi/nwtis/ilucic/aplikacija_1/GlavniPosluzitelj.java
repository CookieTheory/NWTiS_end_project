package org.foi.nwtis.ilucic.aplikacija_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.Konfiguracija;

/**
 * Klasa GlavniPosluzitelj koja je zadužena za otvaranje veze na određenim mrežnim vratima/portu.
 *
 * @author Ivan lucić
 */
public class GlavniPosluzitelj {

  /** Konf - Konfiguracijska datoteka. */
  protected Konfiguracija konf;

  /** Stanje - opcija stanja servera. */
  public static int stanje = 0;

  /** Brojac - opcija brojač za broj obrađenih zahtjeva za udaljenosti. */
  public static int brojac = 0;

  /** Ispis - opcija ispis za ispis komandu na poslužitelju. */
  public static int ispis = 0;

  /** Mrezna Vrata - mrežna vrata poslužitelja - prepisano u konfiguracijskoj datoteci. */
  protected int mreznaVrata = 8000;

  /** Broj Cekaca - broj čekača. */
  private int brojCekaca = 10;

  private static List<Thread> listaDretvi;

  public static ServerSocket posluzitelj;

  /**
   * Kreira Glavnog Poslužitelja.
   *
   * @param konf Konfiguracijska datoteka za glavnog poslužitelja.
   */
  public GlavniPosluzitelj(Konfiguracija konf) {
    this.konf = konf;
    this.mreznaVrata = Integer.parseInt(konf.dajPostavku("posluziteljGlavniVrata"));
    this.brojCekaca = Integer.parseInt(konf.dajPostavku("brojCekaca"));
    GlavniPosluzitelj.listaDretvi = new ArrayList<>();
  }

  /**
   * Pokreće poslužitelja, učitava korisnike, lokacije, uređaja te instancira mapu simulacija.
   * Otvara mrežna vrata.
   */
  public void pokreniPosluzitelja() {
    otvoriMreznaVrata();
  }

  /**
   * Otvara mrežna vrata glavnog poslužitelja, nakon prihvaćanja zahtjeva, kreira dretvu i stavlja
   * joj naziv po zadaći.
   */
  public void otvoriMreznaVrata() {
    try {
      GlavniPosluzitelj.posluzitelj = new ServerSocket(this.mreznaVrata, this.brojCekaca);
      while (true) {
        var uticnica = posluzitelj.accept();
        var dretva = new MrezniRadnik(uticnica, konf);
        var brojDretve = Thread.activeCount() - 1;
        dretva.setName("ilucic_" + brojDretve);
        dretva.start();
        listaDretvi.add(dretva);
      }
    } catch (IOException e) {
      System.out.println("Server ugasen!");
    } finally {
      for (Thread dretva : listaDretvi) {
        try {
          dretva.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
