package info.bitrich.xchangestream.bitpandapro;

import static info.bitrich.xchangestream.bitpandapro.Channels.MARKET_TICKER;
import static info.bitrich.xchangestream.bitpandapro.Channels.ORDER_BOOK;

import com.fasterxml.jackson.databind.JsonNode;
import info.bitrich.xchangestream.bitpandapro.dto.marketdata.BitpandaProStreamingOrderBookSnapshot;
import info.bitrich.xchangestream.bitpandapro.dto.marketdata.BitpandaProStreamingOrderBookUpdate;
import info.bitrich.xchangestream.bitpandapro.dto.marketdata.BitpandaProStreamingTicker;
import info.bitrich.xchangestream.bitpandapro.dto.marketdata.ModifiableOrderBook;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.knowm.xchange.bitpandapro.BitpandaProAdapters;
import org.knowm.xchange.bitpandapro.dto.BitpandaProException;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitpandaProStreamingMarketDataService implements StreamingMarketDataService {
  private static final Logger LOG = LoggerFactory.getLogger(BitpandaProStreamingService.class);
  private static final String ORDER_BOOK_SNAPSHOT = "ORDER_BOOK_SNAPSHOT";
  private static final String ORDER_BOOK_UPDATE = "ORDER_BOOK_UPDATE";

  private BitpandaProStreamingService service;
  private ConcurrentHashMap<CurrencyPair, ModifiableOrderBook> orderBooks =
      new ConcurrentHashMap<>();

  public BitpandaProStreamingMarketDataService(BitpandaProStreamingService service) {
    this.service = service;
  }

  @Override
  public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
    if (!containsPair(service.getProductSubscription().getOrderBook(), currencyPair)) {
      throw new UnsupportedOperationException(
          String.format("The currency pair %s is not subscribed for order book", currencyPair));
    }
    return service
        .subscribeChannel(ORDER_BOOK)
        .filter(jsonNode -> jsonNode.get("channel_name") != null)
        .filter(jsonNode -> jsonNode.get("channel_name").asText().equals(ORDER_BOOK))
        .filter(Objects::nonNull)
        .filter(o -> o.get("type") != null)
        .filter(
            o ->
                BitpandaProAdapters.toInstrumentCode(currencyPair)
                    .equals(o.get("instrument_code").asText()))
        .map(
            o -> {
              String orderBookUpdateType = o.get("type").asText();
              switch (orderBookUpdateType) {
                case ORDER_BOOK_SNAPSHOT:
                  BitpandaProStreamingOrderBookSnapshot bitpandaProStreamingOrderBookSnapshot =
                      BitpandaProStreamingOrderBookSnapshot.fromJsonNode(o);
                  orderBooks.put(
                      currencyPair,
                      new ModifiableOrderBook(
                          bitpandaProStreamingOrderBookSnapshot,
                          BitpandaProSettings.ORDERBOOK_DEPTH));
                  break;
                case ORDER_BOOK_UPDATE:
                  BitpandaProStreamingOrderBookUpdate bitpandaProStreamingOrderBookUpdate =
                      BitpandaProStreamingOrderBookUpdate.fromJsonNode(o);
                  ModifiableOrderBook modifiableOrderBook = orderBooks.get(currencyPair);
                  if (modifiableOrderBook == null) {
                    throw new BitpandaProException(
                        "Received update for the order book but no snapshot yet");
                  }
                  modifiableOrderBook.updateOrderBook(bitpandaProStreamingOrderBookUpdate);
                  break;
                default:
                  throw new BitpandaProException("Unknown order book message type");
              }
              return BitpandaProStreamingAdapters.adaptOrderBook(
                  orderBooks.get(currencyPair), currencyPair);
            });
  }

  @Override
  public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
    if (!containsPair(service.getProductSubscription().getTicker(), currencyPair)) {
      throw new UnsupportedOperationException(
          String.format("The currency pair %s is not subscribed for ticker", currencyPair));
    }
    return service
        .subscribeChannel(MARKET_TICKER)
        .filter(jsonNode -> jsonNode.get("channel_name") != null)
        .filter(jsonNode -> jsonNode.get("channel_name").asText().equals(MARKET_TICKER))
        .filter(Objects::nonNull)
        .map(jsonNode -> jsonNode.get("ticker_updates"))
        .filter(JsonNode::isArray)
        .map(
            jsonNode ->
                (List<Object>)
                    StreamingObjectMapperHelper.getObjectMapper().treeToValue(jsonNode, List.class))
        .flatMap(list -> Observable.fromIterable(list))
        .filter(
            o -> {
              boolean matches =
                  BitpandaProAdapters.toInstrumentCode(currencyPair)
                      .equals(
                          BitpandaProStreamingAdapters.getStringFromJsonMap(
                              (LinkedHashMap) o, "instrument"));
              return matches;
            })
        .map(o -> BitpandaProStreamingTicker.fromJsonMap((LinkedHashMap) o))
        .map(BitpandaProStreamingAdapters::toTicker);
  }

  @Override
  public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
    return null;
  }

  private boolean containsPair(List<CurrencyPair> pairs, CurrencyPair pair) {
    for (CurrencyPair item : pairs) {
      if (item.compareTo(pair) == 0) {
        return true;
      }
    }

    return false;
  }
}
