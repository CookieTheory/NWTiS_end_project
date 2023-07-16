package org.foi.nwtis.ilucic.aplikacija_1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa opcije koja služi za regexove.
 */
public class Opcije {

  /** Sintaksa za status komandu. */
  private static String statusKomandaSintaksa = "^STATUS$";

  /** Sintaksa za kraj komandu. */
  private static String krajKomandaSintaksa = "^KRAJ$";

  /** Sintaksa za init komandu. */
  private static String initKomandaSintaksa = "^INIT$";

  /** Sintaksa za pauza komandu. */
  private static String pauzaKomandaSintaksa = "^PAUZA$";

  /** Sintaksa za info komandu. */
  private static String infoKomandaSintaksa = "^INFO (DA|NE)$";

  /** Sintaksa za udaljenost komandu. */
  private static String udaljenostKomandaSintaksa =
      "^UDALJENOST -?\\d+\\.\\d+ -?\\d+\\.\\d+ -?\\d+\\.\\d+ -?\\d+\\.\\d+$";

  /**
   * Provjera regexa meteo komande.
   *
   * @param ulaz Prima komandu koju šalje mrežni radnik.
   * @return Vraća matcher meteo komande.
   */
  public static Matcher statusKomandaProvjera(String ulaz) {
    Pattern uzorak = Pattern.compile(statusKomandaSintaksa);
    Matcher izlaz = uzorak.matcher(ulaz);
    return izlaz;
  }

  public static Matcher krajKomandaProvjera(String ulaz) {
    Pattern uzorak = Pattern.compile(krajKomandaSintaksa);
    Matcher izlaz = uzorak.matcher(ulaz);
    return izlaz;
  }

  public static Matcher initKomandaProvjera(String ulaz) {
    Pattern uzorak = Pattern.compile(initKomandaSintaksa);
    Matcher izlaz = uzorak.matcher(ulaz);
    return izlaz;
  }

  public static Matcher pauzaKomandaProvjera(String ulaz) {
    Pattern uzorak = Pattern.compile(pauzaKomandaSintaksa);
    Matcher izlaz = uzorak.matcher(ulaz);
    return izlaz;
  }

  public static Matcher infoKomandaProvjera(String ulaz) {
    Pattern uzorak = Pattern.compile(infoKomandaSintaksa);
    Matcher izlaz = uzorak.matcher(ulaz);
    return izlaz;
  }

  public static Matcher udaljenostKomandaProvjera(String ulaz) {
    Pattern uzorak = Pattern.compile(udaljenostKomandaSintaksa);
    Matcher izlaz = uzorak.matcher(ulaz);
    return izlaz;
  }

}
