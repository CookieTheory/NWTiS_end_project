package org.foi.nwtis.podaci;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class FilterKlasa {

  @Getter
  @Setter
  private String vrsta;
  @Getter
  @Setter
  private String aplikacija;
  @Getter
  @Setter
  private String requestUrl;
  @Getter
  @Setter
  private Timestamp vrijeme;


  public FilterKlasa() {}
}
