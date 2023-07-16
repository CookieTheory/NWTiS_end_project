package org.foi.nwtis.podaci;

public class NadzorKlasa {
  private int status;
  private String opis;

  public NadzorKlasa(int status, String opis) {
    super();
    this.setStatus(status);
    this.setOpis(opis);
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getOpis() {
    return opis;
  }

  public void setOpis(String opis) {
    this.opis = opis;
  }
}
