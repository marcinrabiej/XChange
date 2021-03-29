package info.bitrich.xchangestream.bitpandapro.dto.marketdata;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderBookEntry {
  private final BigDecimal price;
  private final BigDecimal volume;
}
