package org.knowm.xchange.bitpandapro;

import java.math.BigDecimal;
import java.util.*;
import org.knowm.xchange.bitpandapro.dto.marketdata.*;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;
import org.knowm.xchange.dto.trade.LimitOrder;

public class BitpandaProAdapters {

  private static final String CURRENCY_SEPARATOR = "_";

  private BitpandaProAdapters() {}

  public static CurrencyPair toCurrencyPair(final String productId) {
    final String[] parts = productId.split(CURRENCY_SEPARATOR);
    return new CurrencyPair(parts[0], parts[1]);
  }

  public static String toInstrumentCode(CurrencyPair currencyPair) {
    return currencyPair.base + CURRENCY_SEPARATOR + currencyPair.counter;
  }

  public static Ticker adaptTicker(BitpandaProTicker ticker, CurrencyPair currencyPair) {

    BigDecimal last = ticker.getLastPrice();
    BigDecimal high = ticker.getHigh();
    BigDecimal low = ticker.getLow();
    BigDecimal buy = ticker.getBestBid();
    BigDecimal sell = ticker.getBestAsk();
    BigDecimal volume = ticker.getBaseVolume();
    Date date = ticker.getTime();
    BigDecimal quoteVolume = ticker.getQuoteVolume();

    return new Ticker.Builder()
        .instrument(currencyPair)
        .last(last)
        .high(high)
        .low(low)
        .bid(buy)
        .ask(sell)
        .volume(volume)
        .quoteVolume(quoteVolume)
        .timestamp(date)
        .build();
  }

  public static OrderBook adaptOrderBook(
      BitpandaProOrderBook book, CurrencyPair currencyPair, Date date) {

    List<LimitOrder> asks = toLimitOrderList(book.getAsks(), OrderType.ASK, currencyPair);
    List<LimitOrder> bids = toLimitOrderList(book.getBids(), OrderType.BID, currencyPair);

    return new OrderBook(date, asks, bids);
  }

  public static OrderBook adaptOrderBook(BitpandaProOrderBook book, CurrencyPair currencyPair) {
    return adaptOrderBook(book, currencyPair, book.getTime());
  }

  private static List<LimitOrder> toLimitOrderList(
      BitpandaProOrderBookEntry[] levels, OrderType orderType, CurrencyPair currencyPair) {

    List<LimitOrder> allLevels = new ArrayList<>();

    if (levels != null) {
      for (int i = 0; i < levels.length; i++) {
        BitpandaProOrderBookEntry bitpandaProOrderBookEntry = levels[i];

        allLevels.add(
            new LimitOrder(
                orderType,
                bitpandaProOrderBookEntry.getVolume(),
                currencyPair,
                "0",
                null,
                bitpandaProOrderBookEntry.getPrice()));
      }
    }

    return allLevels;
  }

  public static ExchangeMetaData adaptToExchangeMetaData(
      ExchangeMetaData exchangeMetaData,
      List<BitpandaProInstrument> bitpandaProInstruments,
      List<BitpandaProCurrency> bitpandaProCurrencies) {

    Map<CurrencyPair, CurrencyPairMetaData> currencyPairs =
        exchangeMetaData == null ? new HashMap<>() : exchangeMetaData.getCurrencyPairs();

    Map<org.knowm.xchange.currency.Currency, CurrencyMetaData> currencies =
        exchangeMetaData == null ? new HashMap<>() : exchangeMetaData.getCurrencies();

    for (BitpandaProInstrument bitpandaProInstrument : bitpandaProInstruments) {
      if (!bitpandaProInstrument.getState().equals("ACTIVE")) {
        continue;
      }

      BigDecimal counterMinimumAmount = bitpandaProInstrument.getMinSize();
      CurrencyPair pair =
          new CurrencyPair(
              bitpandaProInstrument.getBase().getCode(),
              bitpandaProInstrument.getQuote().getCode());

      CurrencyPairMetaData staticMetaData = currencyPairs.get(pair);
      int baseScale = bitpandaProInstrument.getAmountPrecision();
      int priceScale = bitpandaProInstrument.getMarketPrecision();

      CurrencyPairMetaData cpmd =
          new CurrencyPairMetaData(
              new BigDecimal("0.15"),
              null,
              null,
              counterMinimumAmount,
              null,
              baseScale,
              priceScale,
              staticMetaData != null ? staticMetaData.getFeeTiers() : null,
              null,
              pair.counter,
              true);
      currencyPairs.put(pair, cpmd);
    }

    bitpandaProCurrencies.forEach(
        bitpandaProCurrency -> {
          Currency cur = adaptCurrency(bitpandaProCurrency);
          currencies.put(
              cur,
              new CurrencyMetaData(
                  bitpandaProCurrency.getPrecision(), BigDecimal.ZERO, null, WalletHealth.ONLINE));
        });

    return new ExchangeMetaData(
        currencyPairs,
        currencies,
        exchangeMetaData == null ? null : exchangeMetaData.getPublicRateLimits(),
        exchangeMetaData == null ? null : exchangeMetaData.getPrivateRateLimits(),
        true);
  }

  private static Currency adaptCurrency(BitpandaProCurrency currency) {
    return new Currency(currency.getCode());
  }
}
