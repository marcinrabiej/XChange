package org.knowm.xchange.bitpandapro.dto.marketdata;

import java.math.BigDecimal;
import lombok.Data;

@Data
public abstract class BitpandaProOrderBookEntry {

  private final BigDecimal price;
  private final BigDecimal volume;

  public BitpandaProOrderBookEntry(BigDecimal price, BigDecimal volume) {

    this.price = price;
    this.volume = volume;
  }
}
