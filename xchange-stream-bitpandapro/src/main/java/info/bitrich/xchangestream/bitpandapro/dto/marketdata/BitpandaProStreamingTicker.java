package info.bitrich.xchangestream.bitpandapro.dto.marketdata;

import info.bitrich.xchangestream.bitpandapro.BitpandaProStreamingAdapters;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import lombok.Data;

@Data
public class BitpandaProStreamingTicker {

  private final String instrument;
  private final BigDecimal lastPrice;
  private final BigDecimal priceChange;
  private final BigDecimal priceChangePercent;
  private final BigDecimal high;
  private final BigDecimal low;
  private final BigDecimal volume;

  private BitpandaProStreamingTicker(
      String instrument,
      BigDecimal lastPrice,
      BigDecimal priceChange,
      BigDecimal priceChangePercent,
      BigDecimal high,
      BigDecimal low,
      BigDecimal volume) {
    this.instrument = instrument;
    this.lastPrice = lastPrice;
    this.priceChange = priceChange;
    this.priceChangePercent = priceChangePercent;
    this.volume = volume;
    this.high = high;
    this.low = low;
  }

  public static BitpandaProStreamingTicker fromJsonMap(LinkedHashMap map) {
    return new BitpandaProStreamingTicker(
        BitpandaProStreamingAdapters.getStringFromJsonMap(map, "instrument"),
        BitpandaProStreamingAdapters.getBigDecimalFromJsonMap(map, "last_price"),
        BitpandaProStreamingAdapters.getBigDecimalFromJsonMap(map, "price_change"),
        BitpandaProStreamingAdapters.getBigDecimalFromJsonMap(map, "price_change_percent"),
        BitpandaProStreamingAdapters.getBigDecimalFromJsonMap(map, "high"),
        BitpandaProStreamingAdapters.getBigDecimalFromJsonMap(map, "low"),
        BitpandaProStreamingAdapters.getBigDecimalFromJsonMap(map, "volume"));
  }
}
