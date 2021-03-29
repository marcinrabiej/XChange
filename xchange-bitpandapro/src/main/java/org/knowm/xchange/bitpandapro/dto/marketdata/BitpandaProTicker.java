package org.knowm.xchange.bitpandapro.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class BitpandaProTicker {

  private final String instrumentCode;
  private final Integer sequence;
  private final String state;
  private final Date time;
  private final boolean isFrozen;
  private final BigDecimal quoteVolume;
  private final BigDecimal baseVolume;
  private final BigDecimal lastPrice;
  private final BigDecimal bestBid;
  private final BigDecimal bestAsk;
  private final BigDecimal high;
  private final BigDecimal low;

  public BitpandaProTicker(
      @JsonProperty("instrument_code") String instrumentCode,
      @JsonProperty("sequence") Integer sequence,
      @JsonProperty("state") String state,
      @JsonProperty("time") Date time,
      @JsonProperty("is_frozen") boolean isFrozen,
      @JsonProperty("quote_volume") BigDecimal quoteVolume,
      @JsonProperty("base_volume") BigDecimal baseVolume,
      @JsonProperty("last_price") BigDecimal lastPrice,
      @JsonProperty("best_bid") BigDecimal bestBid,
      @JsonProperty("best_ask") BigDecimal bestAsk,
      @JsonProperty("high") BigDecimal high,
      @JsonProperty("low") BigDecimal low) {
    this.instrumentCode = instrumentCode;
    this.sequence = sequence;
    this.state = state;
    this.time = time;
    this.isFrozen = isFrozen;
    this.quoteVolume = quoteVolume;
    this.baseVolume = baseVolume;
    this.lastPrice = lastPrice;
    this.bestBid = bestBid;
    this.bestAsk = bestAsk;
    this.high = high;
    this.low = low;
  }
}
