package org.foi.nwtis.ilucic.konfiguracije;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/**
 * Klasa konfiguracija za rad s postavkama konfiguracije u bin formatu
 * 
 * @author Ivan Lucić
 */
public class KonfiguracijaBin extends KonfiguracijaApstraktna {

  /** Konstanta BIN */
  public static final String TIP = "bin";

  /**
   * Konstruktor za inicijalizaciju KonfiguracijeBIN
   * 
   * @param nazivDatoteke - Naziv datoteke
   */
  public KonfiguracijaBin(String nazivDatoteke) {
    super(nazivDatoteke);
  }

  /**
   * Sprema konfiguraciju na disk u bin formatu. Izvor:
   * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/ObjectOutputStream.html
   * 
   * @param datoteka - Naziv datoteke
   * @throws NeispravnaKonfiguracija - iznimka kada nešto ne valja
   */
  @Override
  public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {
    var putanja = Path.of(datoteka);
    var tip = Konfiguracija.dajTipKonfiguracije(datoteka);

    if (tip == null || tip.compareTo(TIP) != 0) {
      throw new NeispravnaKonfiguracija("Datoteka '" + datoteka + "' nije tip " + TIP);
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isWritable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' je direktorij ili nije moguće pisati.");
    }

    try {
      ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(putanja));
      oos.writeObject(this.postavke);
      oos.close();
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće upisivati." + e.getMessage());
    }
  }


  /**
   * Učitaj konfiguraciju sa diska iz bin datoteke. Izvor:
   * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/ObjectInputStream.html
   * 
   * @throws NeispravnaKonfiguracija - ako se ne može čitati ili nije ispravna datoteka.
   */
  @Override
  public void ucitajKonfiguraciju() throws NeispravnaKonfiguracija {
    var datoteka = this.nazivDatoteke;
    var putanja = Path.of(datoteka);
    var tip = Konfiguracija.dajTipKonfiguracije(datoteka);

    if (tip == null || tip.compareTo(TIP) != 0) {
      throw new NeispravnaKonfiguracija("Datoteka '" + datoteka + "' nije tip " + TIP);
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isReadable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' je direktorij ili nije moguće čitati.");
    }

    try {
      ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(putanja));
      Object objekt;
      objekt = ois.readObject();
      this.postavke = (Properties) objekt;
      ois.close();
    } catch (IOException | ClassNotFoundException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće upisivati." + e.getMessage());
    }
  }

}
