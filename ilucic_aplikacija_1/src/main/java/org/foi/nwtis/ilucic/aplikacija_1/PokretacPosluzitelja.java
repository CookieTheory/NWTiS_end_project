package org.foi.nwtis.ilucic.aplikacija_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/**
 * Klasa pokretača poslužitelja.
 */
public class PokretacPosluzitelja {

  /**
   * Glavna metoda Pokretača poslužitelja.
   *
   * @param args Argumenti poslani u komandoj liniji.
   */
  public static void main(String[] args) {
    var pokretac = new PokretacPosluzitelja();
    if (!pokretac.provjeriArgumente(args)) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke");
      return;
    }
    try {
      var konf = pokretac.ucitajPostavke(args[0]);
      var glavniPosluzitelj = new GlavniPosluzitelj(konf);
      glavniPosluzitelj.pokreniPosluzitelja();
    } catch (NeispravnaKonfiguracija e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Pogreška kod učitavanja postavki iz datoteke(" + e.getMessage() + ")");
    }
  }

  /**
   * Provjerava argumente komandne linije.
   *
   * @param args Argumenti poslani u komandoj liniji.
   * @return true, ako je samo jedna vrijednost.
   */
  private boolean provjeriArgumente(String[] args) {
    return args.length == 1 ? true : false;
  }

  /**
   * Učitaj konfiguracijsku datoteku, odnosno preuzmi.
   *
   * @param nazivDatoteke Naziv datoteke konfiguracijske datoteke.
   * @return Konfiguraciju konfiguracijske datoteke.
   * @throws NeispravnaKonfiguracija Baca neispravnu konfiguraciju ako se dogodi greška.
   */
  Konfiguracija ucitajPostavke(String nazivDatoteke) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
  }

}
