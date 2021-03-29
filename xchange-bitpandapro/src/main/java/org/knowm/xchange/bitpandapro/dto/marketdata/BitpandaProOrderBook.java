package org.knowm.xchange.bitpandapro.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.Data;

@Data
public class BitpandaProOrderBook {

  private final BitpandaProOrderBookEntry[] bids;
  private final BitpandaProOrderBookEntry[] asks;
  private final String instrumentCode;
  private final Date time;
  private final Long sequence;

  public BitpandaProOrderBook(
      @JsonProperty("instrument_code") String instrumentCode,
      @JsonProperty("time") Date time,
      @JsonProperty("bids") Map[] bids,
      @JsonProperty("asks") Map[] asks,
      @JsonProperty("sequence") Long sequence) {
    this.instrumentCode = instrumentCode;
    this.time = time;
    this.sequence = sequence;

    if (bids != null && bids.length > 0) {
      this.bids = new BitpandaProOrderBookEntry[bids.length];
      for (int i = 0; i < bids.length; i++) {
        this.bids[i] = convertToBookEntry(bids[i]);
      }
    } else {
      this.bids = null;
    }

    if (asks != null && asks.length > 0) {
      this.asks = new BitpandaProOrderBookEntry[asks.length];
      for (int i = 0; i < asks.length; i++) {
        this.asks[i] = convertToBookEntry(asks[i]);
      }
    } else {
      this.asks = null;
    }
  }

  private static BitpandaProOrderBookEntry convertToBookEntry(Map entry) {
    if (entry != null && entry.containsKey("order_id")) {
      BigDecimal price = BigDecimal.valueOf(Double.valueOf((String) entry.get("price")));
      BigDecimal volume = BigDecimal.valueOf(Double.valueOf((String) entry.get("amount")));
      String orderId = (String) entry.get("order_id");
      return new BitpandaProOrderBookEntryLevel3(price, volume, orderId);
    } else if (entry != null && entry.containsKey("number_of_orders")) {
      BigDecimal price = BigDecimal.valueOf(Double.valueOf((String) entry.get("price")));
      BigDecimal volume = BigDecimal.valueOf(Double.valueOf((String) entry.get("amount")));
      Integer numberOfOrders = Integer.valueOf((String) entry.get("number_of_orders"));
      return new BitpandaProOrderBookEntryLevel1or2(price, volume, numberOfOrders);
    }
    return null;
  }
}
