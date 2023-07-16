package org.foi.nwtis.podaci;

public class PogresnaAutentikacija extends Exception {

  private static final long serialVersionUID = -511882154905193792L;

  public PogresnaAutentikacija(String message) {
    super(message);
  }

  public PogresnaAutentikacija() {}
}
