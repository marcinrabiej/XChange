package org.knowm.xchange.bitpandapro.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BitpandaProCurrency {

  private final String code;
  private final Integer precision;

  public BitpandaProCurrency(
      @JsonProperty("code") String code, @JsonProperty("precision") Integer precision) {
    this.code = code;
    this.precision = precision;
  }
}
