package org.knowm.xchange.bitpandapro.dto.marketdata;

import java.math.BigDecimal;

public class BitpandaProOrderBookEntryLevel3 extends BitpandaProOrderBookEntry {

  private final String orderId;

  public BitpandaProOrderBookEntryLevel3(BigDecimal price, BigDecimal volume, String orderId) {
    super(price, volume);
    this.orderId = orderId;
  }

  public String getOrderId() {
    return orderId;
  }
}
