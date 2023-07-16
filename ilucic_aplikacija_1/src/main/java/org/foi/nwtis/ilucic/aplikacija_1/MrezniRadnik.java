package org.foi.nwtis.ilucic.aplikacija_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import org.foi.nwtis.Konfiguracija;

/**
 * Klasa Mrežni radnik.
 */
public class MrezniRadnik extends Thread {

  /** Mrezna uticnica - mrežna utičnica mrežnog radnika. */
  protected Socket mreznaUticnica;

  /** Konf - Konfiguracijska datoteka. */
  protected Konfiguracija konfig;

  /**
   * Instancira novog mrežnog radnika. Konstruktor s nastave.
   *
   * @param mreznaUticnica Mrežna utičnica na koju se mrežni radnik priključuje.
   * @param konfig Konfiguracijska datoteka.
   */
  public MrezniRadnik(Socket mreznaUticnica, Konfiguracija konfig) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.konfig = konfig;
  }

  /**
   * Početak.
   */
  @Override
  public synchronized void start() {
    super.start();
  }

  /**
   * Pokreće mrežnog radnika.
   */
  @Override
  public void run() {
    try {
      var citac = new BufferedReader(
          new InputStreamReader(this.mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(this.mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        if (GlavniPosluzitelj.ispis == 1)
          System.out.println(red);
        poruka.append(red);
      }
      this.mreznaUticnica.shutdownInput();
      String odgovor = this.obradiZahtjev(poruka.toString());
      pisac.write(odgovor);
      pisac.flush();
      this.mreznaUticnica.shutdownOutput();
      this.mreznaUticnica.close();
    } catch (IOException e) {
    }
  }

  /**
   * Obrađuje zahtjev, odnosno primljenu komandu u obliku KORISNIK korisnik LOZINKA lozinka i
   * predmetni dio.
   *
   * @param zahtjev Zahtjev odnosno primljena komanda.
   * @return String koji će biti odgovor.
   * @throws IOException
   */
  private String obradiZahtjev(String zahtjev) throws IOException {
    if (Opcije.statusKomandaProvjera(zahtjev).matches()) {
      return odgovoriStatus();
    }
    if (Opcije.krajKomandaProvjera(zahtjev).matches()) {
      return odgovoriKraj();
    }
    if (Opcije.initKomandaProvjera(zahtjev).matches()) {
      return odgovoriInit();
    }
    if (Opcije.pauzaKomandaProvjera(zahtjev).matches()) {
      return odgovoriPauza();
    }
    if (Opcije.infoKomandaProvjera(zahtjev).matches()) {
      return odgovoriInfo(zahtjev);
    }
    if (Opcije.udaljenostKomandaProvjera(zahtjev).matches()) {
      return odgovoriUdaljenost(zahtjev);
    }
    return "ERROR 05 : Format komande nije valjan!";
  }

  private String odgovoriStatus() {
    String odgovor = "";
    int stanje = GlavniPosluzitelj.stanje;
    if (stanje == 0) {
      odgovor = "OK 0";
    } else if (stanje == 1) {
      odgovor = "OK 1";
    } else {
      odgovor = "ERROR 05 : Nepoznata greška sa statusom servera";
    }
    return odgovor;
  }

  private String odgovoriKraj() throws IOException {
    String odgovor = "OK";
    var pisac = new BufferedWriter(
        new OutputStreamWriter(this.mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));
    pisac.write(odgovor);
    pisac.flush();
    this.mreznaUticnica.shutdownOutput();
    this.mreznaUticnica.close();
    GlavniPosluzitelj.posluzitelj.close();
    return odgovor;
  }

  private String odgovoriInit() {
    String odgovor = "";
    if (GlavniPosluzitelj.stanje == 0) {
      GlavniPosluzitelj.stanje = 1;
      GlavniPosluzitelj.brojac = 0;
      odgovor = "OK";
    } else if (GlavniPosluzitelj.stanje == 1) {
      odgovor = "ERROR 02 : Poslužitelj je već aktivan";
    }
    return odgovor;
  }

  private String odgovoriPauza() {
    String odgovor = "";
    if (GlavniPosluzitelj.stanje == 1) {
      GlavniPosluzitelj.stanje = 0;
      odgovor = "OK " + GlavniPosluzitelj.brojac;
    } else if (GlavniPosluzitelj.stanje == 0) {
      odgovor = "ERROR 01 : Poslužitelj je pauziran";
    }
    return odgovor;
  }

  private String odgovoriInfo(String zahtjev) {
    if (GlavniPosluzitelj.stanje == 0)
      return "ERROR 01 : Poslužitelj je pauziran";
    String odgovor = "";
    if (zahtjev.contains("DA")) {
      if (GlavniPosluzitelj.ispis == 0) {
        GlavniPosluzitelj.ispis = 1;
        odgovor = "OK";
      } else if (GlavniPosluzitelj.ispis == 1) {
        odgovor = "ERROR 03 : Poslužitelj već ispisuje komande.";
      }
    } else if (zahtjev.contains("NE")) {
      if (GlavniPosluzitelj.ispis == 0) {
        odgovor = "ERROR 04 : Poslužitelj već NE ispisuje komande.";
      } else if (GlavniPosluzitelj.ispis == 1) {
        GlavniPosluzitelj.ispis = 0;
        odgovor = "OK";
      }
    }
    return odgovor;
  }

  private String odgovoriUdaljenost(String zahtjev) {
    if (GlavniPosluzitelj.stanje == 0)
      return "ERROR 01 : Poslužitelj je pauziran";
    String odgovor = "";
    String[] podijeljeno = zahtjev.split(" ");
    double gpsSirina1 = Double.parseDouble(podijeljeno[1]);
    double gpsDuzina1 = Double.parseDouble(podijeljeno[2]);
    double gpsSirina2 = Double.parseDouble(podijeljeno[3]);
    double gpsDuzina2 = Double.parseDouble(podijeljeno[4]);
    String udaljenost = izracunajUdaljenost(gpsSirina1, gpsDuzina1, gpsSirina2, gpsDuzina2);
    odgovor = "OK " + udaljenost;
    GlavniPosluzitelj.brojac++;
    return odgovor;
  }

  // https://keisan.casio.com/exec/system/1224587128
  // 6371 je medijan udaljenost na zemlji
  private String izracunajUdaljenost(double sirina1, double duzina1, double sirina2,
      double duzina2) {
    double gpsSirina1 = Math.toRadians(sirina1);
    double gpsDuzina1 = Math.toRadians(duzina1);
    double gpsSirina2 = Math.toRadians(sirina2);
    double gpsDuzina2 = Math.toRadians(duzina2);

    double udaljenostSirina = gpsSirina2 - gpsSirina1;
    double udaljenostDuzina = gpsDuzina2 - gpsDuzina1;
    double a = Math.pow(Math.sin(udaljenostSirina / 2), 2)
        + Math.cos(gpsSirina1) * Math.cos(gpsSirina2) * Math.pow(Math.sin(udaljenostDuzina / 2), 2);

    double c = 2 * Math.asin(Math.sqrt(a));

    double medijanRadijus = 6371;
    double rezultat = c * medijanRadijus;

    String konacnaUdaljenost = String.format("%7.2f", rezultat);
    konacnaUdaljenost = konacnaUdaljenost.trim();

    return konacnaUdaljenost;
  }

  /**
   * Prekid mrežnog radnika.
   */
  @Override
  public void interrupt() {
    super.interrupt();
  }

}
