package org.knowm.xchange.bitpandapro.service;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bitpandapro.BitpandaProAdapters;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProOrderBook;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProTicker;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.exceptions.RateLimitExceededException;
import org.knowm.xchange.service.marketdata.MarketDataService;

@Slf4j
public class BitpandaProMarketDataService extends BitpandaProMarketDataServiceRaw
    implements MarketDataService {

  public BitpandaProMarketDataService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public Ticker getTicker(CurrencyPair currencyPair, Object... args)
      throws IOException, RateLimitExceededException {

    // Request data
    BitpandaProTicker ticker = getBitpandaProProductTicker(currencyPair);

    // Adapt to XChange DTOs
    return BitpandaProAdapters.adaptTicker(ticker, currencyPair);
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args)
      throws IOException, RateLimitExceededException {

    int level = 3; // full order book
    int depth = 50;
    if (args != null && args.length > 0) {
      // parameter 1, if present, is the level
      if (args[0] instanceof Number) {
        Number arg = (Number) args[0];
        level = arg.intValue();
      } else {
        throw new IllegalArgumentException(
            "Extra argument #1, the 'level', must be an int (was " + args[0].getClass() + ")");
      }
      if (args.length > 1) {
        // parameter 2, if present, is the depth
        if (args[0] instanceof Number) {
          Number arg = (Number) args[1];
          depth = arg.intValue();
        } else {
          throw new IllegalArgumentException(
              "Extra argument #2, the 'depth', must be an int (was " + args[0].getClass() + ")");
        }
      }
    }

    BitpandaProOrderBook bitpandaProOrderBook = getBitpandaProOrderBook(currencyPair, level, depth);
    return BitpandaProAdapters.adaptOrderBook(bitpandaProOrderBook, currencyPair);
  }
}
