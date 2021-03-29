package info.bitrich.xchangestream.bitpandapro;

import info.bitrich.xchangestream.bitpandapro.dto.marketdata.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.knowm.xchange.bitpandapro.BitpandaProAdapters;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;

public class BitpandaProStreamingAdapters {

  public static Ticker toTicker(BitpandaProStreamingTicker bitpandaProStreamingTicker) {

    BigDecimal last = bitpandaProStreamingTicker.getLastPrice();
    BigDecimal high = bitpandaProStreamingTicker.getHigh();
    BigDecimal low = bitpandaProStreamingTicker.getLow();
    BigDecimal volume = bitpandaProStreamingTicker.getVolume();
    return new Ticker.Builder()
        .instrument(BitpandaProAdapters.toCurrencyPair(bitpandaProStreamingTicker.getInstrument()))
        .last(last)
        .high(high)
        .low(low)
        .volume(volume)
        .build();
  }

  public static String getStringFromJsonMap(LinkedHashMap map, String key) {
    Object value = map.get(key);
    if (value != null) {
      return value.toString();
    } else {
      return null;
    }
  }

  public static BigDecimal getBigDecimalFromJsonMap(LinkedHashMap map, String key) {
    Object value = map.get(key);
    if (value != null) {
      return getBigDecimalFromString(value.toString());
    } else {
      return null;
    }
  }

  private static List<LimitOrder> adaptOrders(
      List<OrderBookEntry> orders, Order.OrderType orderType, CurrencyPair currencyPair) {
    return orders.stream()
        .map(adaptOrderBookEntry(orderType, currencyPair))
        .collect(Collectors.toList());
  }

  private static Function<OrderBookEntry, LimitOrder> adaptOrderBookEntry(
      Order.OrderType orderType, CurrencyPair currencyPair) {
    return obe ->
        new LimitOrder(orderType, obe.getVolume(), currencyPair, null, null, obe.getPrice());
  }

  public static BigDecimal getBigDecimalFromString(String s) {
    return BigDecimal.valueOf(Double.valueOf(s));
  }

  public static OrderBook adaptOrderBook(
      ModifiableOrderBook modifiableOrderBook, CurrencyPair currencyPair) {
    return new OrderBook(
        modifiableOrderBook.getTime(),
        new ArrayList<>(
            adaptOrders(
                modifiableOrderBook.getAsks().getOrderedOrdersList(),
                Order.OrderType.ASK,
                currencyPair)),
        new ArrayList<>(
            adaptOrders(
                modifiableOrderBook.getBids().getOrderedOrdersList(),
                Order.OrderType.BID,
                currencyPair)));
  }
}
