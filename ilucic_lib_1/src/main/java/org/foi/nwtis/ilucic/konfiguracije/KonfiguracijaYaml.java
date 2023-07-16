package org.foi.nwtis.ilucic.konfiguracije;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.api.YamlOutputStreamWriter;

/**
 * Klasa konfiguracija za rad s postavkama konfiguracije u yaml formatu
 * 
 * @author Ivan Lucić
 */
public class KonfiguracijaYaml extends KonfiguracijaApstraktna {

  /** Konstanta TIP */
  public static final String TIP = "yaml";


  /**
   * Konstruktor za inicijalizaciju KonfiguracijeTXT
   * 
   * @param nazivDatoteke - Naziv datoteke
   */
  public KonfiguracijaYaml(String nazivDatoteke) {
    super(nazivDatoteke);
  }


  /**
   * Sprema konfiguraciju na disk u yaml formatu. Izvor:
   * https://bitbucket.org/snakeyaml/snakeyaml-engine/wiki/Documentation#markdown-header-dumping-yaml
   * YamlOutputStreamWriter ima override za IOExcepction, ali ako se ne upotpuni i dalje ce ga
   * bacati te ga preuzima try catch block s NeispravnaKonfiguracija kao što je zadano.
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
      Charset znakovi = Charset.forName("UTF-8");
      YamlOutputStreamWriter yamlizlaz =
          new YamlOutputStreamWriter(Files.newOutputStream(putanja), znakovi) {
            @Override
            public void processIOException(IOException e) {}
          };
      DumpSettings postavke = DumpSettings.builder().build();
      Dump ispisi = new Dump(postavke);
      ispisi.dump(this.postavke, yamlizlaz);
      yamlizlaz.close();
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće upisivati." + e.getMessage());
    }
  }


  /**
   * Učitaj konfiguraciju sa diska iz XML datoteke. Izvor:
   * https://bitbucket.org/snakeyaml/snakeyaml-engine/wiki/Documentation#markdown-header-loading-yaml
   * Za properties korišteno putAll pošto je super klasa hashtable koje load iz yamla vraća:
   * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Properties.html
   * 
   * @throws NeispravnaKonfiguracija - ako nije ispravna
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
          "Datoteka '" + datoteka + "' je direktorij ili nije moguće pisati.");
    }

    try {
      LoadSettings postavke = LoadSettings.builder().build();
      Load ucitaj = new Load(postavke);
      this.postavke.putAll((Map<?, ?>) ucitaj.loadFromInputStream(Files.newInputStream(putanja)));
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće upisivati." + e.getMessage());
    }
  }

}
