package org.knowm.xchange.bitpandapro.service.marketdata;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitpandapro.BitpandaProExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.service.marketdata.MarketDataService;

public class OrderBookFetchIntegrationTest {

  @Test
  public void orderBookFetchTest() throws Exception {

    Exchange exchange = ExchangeFactory.INSTANCE.createExchange(BitpandaProExchange.class);
    MarketDataService marketDataService = exchange.getMarketDataService();
    OrderBook orderBook = marketDataService.getOrderBook(new CurrencyPair("BTC", "EUR"));
    System.out.println(orderBook.toString());
    assertThat(orderBook).isNotNull();
  }
}
