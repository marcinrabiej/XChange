package info.bitrich.xchangestream.bitpandapro;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitpandaProManualExample {
  private static final Logger LOG = LoggerFactory.getLogger(BitpandaProManualExample.class);

  public static void main(String[] args) {

    // Far safer than temporarily adding these to code that might get committed to VCS
    String apiKey = System.getProperty("bitpandapro-api-key");
    String apiSecret = System.getProperty("bitpandapro-api-secret");
    String apiPassphrase = System.getProperty("bitpandapro-api-passphrase");

    ExchangeSpecification spec =
        StreamingExchangeFactory.INSTANCE
            .createExchange(BitpandaProStreamingExchange.class)
            .getDefaultExchangeSpecification();
    spec.setApiKey(apiKey);
    spec.setSecretKey(apiSecret);
    spec.setExchangeSpecificParametersItem("passphrase", apiPassphrase);
    BitpandaProStreamingExchange exchange =
        (BitpandaProStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(spec);

    ProductSubscription productSubscription =
        ProductSubscription.create()
            .addTicker(CurrencyPair.BTC_EUR)
            .addTicker(CurrencyPair.ETH_EUR)
            .addOrderbook(CurrencyPair.BTC_EUR)
            .addOrderbook(CurrencyPair.ETH_EUR)
            .build();

    exchange.connect(productSubscription).blockingAwait();

    exchange
        .getStreamingMarketDataService()
        .getOrderBook(CurrencyPair.BTC_EUR)
        .subscribe(
            orderBook -> {
              LOG.info("ORDERBOOK BTC_EUR: {}", orderBook);
            },
            throwable -> LOG.error("ERROR in getting order book: ", throwable));

    exchange
        .getStreamingMarketDataService()
        .getOrderBook(CurrencyPair.ETH_EUR)
        .subscribe(
            orderBook -> {
              LOG.info("ORDERBOOK ETH_EUR: {}", orderBook);
            },
            throwable -> LOG.error("ERROR in getting order book: ", throwable));

    exchange
        .getStreamingMarketDataService()
        .getTicker(CurrencyPair.BTC_EUR)
        .subscribe(
            ticker -> {
              LOG.info("TICKER BTC_EUR: {}", ticker);
            },
            throwable -> LOG.error("ERROR in getting ticker: ", throwable));

    exchange
        .getStreamingMarketDataService()
        .getTicker(CurrencyPair.ETH_EUR)
        .subscribe(
            ticker -> {
              LOG.info("TICKER ETH_EUR: {}", ticker);
            },
            throwable -> LOG.error("ERROR in getting ticker: ", throwable));
    /*
    exchange
        .getStreamingMarketDataService()
        .getTrades(CurrencyPair.BTC_EUR)
        .subscribe(
            trade -> {
              LOG.info("TRADE: {}", trade);
            },
            throwable -> LOG.error("ERROR in getting trades: ", throwable));

      exchange
          .getStreamingTradeService()
          .getUserTrades(CurrencyPair.BTC_EUR)
          .subscribe(
              trade -> {
                LOG.info("USER TRADE: {}", trade);
              },
              throwable -> LOG.error("ERROR in getting user trade: ", throwable));

      exchange
          .getStreamingTradeService()
          .getOrderChanges(CurrencyPair.BTC_EUR)
          .subscribe(
              order -> {
                LOG.info("USER ORDER: {}", order);
              },
              throwable -> LOG.error("ERROR in getting user orders: ", throwable));
    */

    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    exchange.disconnect().blockingAwait();
  }
}
