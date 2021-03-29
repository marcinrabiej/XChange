package org.knowm.xchange.bitpandapro.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BitpandaProInstrument {

  private final BitpandaProCurrency base;
  private final BitpandaProCurrency quote;
  private final BigDecimal minSize;
  private final Integer amountPrecision;
  private final Integer marketPrecision;
  private final String state;

  public BitpandaProInstrument(
      @JsonProperty("base") BitpandaProCurrency base,
      @JsonProperty("quote") BitpandaProCurrency quote,
      @JsonProperty("min_size") BigDecimal minSize,
      @JsonProperty("amount_precision") Integer amountPrecision,
      @JsonProperty("market_precision") Integer marketPrecision,
      @JsonProperty("state") String state) {
    this.base = base;
    this.quote = quote;
    this.minSize = minSize;
    this.amountPrecision = amountPrecision;
    this.marketPrecision = marketPrecision;
    this.state = state;
  }
}
