package org.knowm.xchange.bitpandapro.dto.marketdata;

import java.math.BigDecimal;

public class BitpandaProOrderBookEntryLevel1or2 extends BitpandaProOrderBookEntry {

  private final int numOrdersOnLevel;

  public BitpandaProOrderBookEntryLevel1or2(
      BigDecimal price, BigDecimal volume, int numOrdersOnLevel) {
    super(price, volume);
    this.numOrdersOnLevel = numOrdersOnLevel;
  }

  public int getNumOrdersOnLevel() {
    return numOrdersOnLevel;
  }
}
