package info.bitrich.xchangestream.bitpandapro.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.bitpandapro.BitpandaProSettings;
import info.bitrich.xchangestream.core.ProductSubscription;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.knowm.xchange.bitpandapro.dto.account.BitpandaProWebsocketAuthData;
import org.knowm.xchange.currency.CurrencyPair;

public class BitpandaProWebSocketSubscriptionMessage {

  public static final String TYPE = "type";
  public static final String CHANNELS = "channels";
  public static final String INSTRUMENT_CODES = "instrument_codes";
  public static final String NAME = "name";
  public static final String PRICE_POINTS_MODE = "price_points_mode";
  public static final String DEPTH = "depth";

  // if authenticating
  public static final String SIGNATURE = "signature";
  public static final String KEY = "key";
  public static final String PASSPHRASE = "passphrase";
  public static final String TIMESTAMP = "timestamp";
  public static final String ORDER_BOOK = "ORDER_BOOK";
  public static final String MARKET_TICKER = "MARKET_TICKER";
  public static final String TRADES = "PRICE_TICKS";

  class BitpandaProProductSubscription {
    @JsonProperty(NAME)
    private final String name;

    @JsonProperty(INSTRUMENT_CODES)
    private final String[] instrumentCodes;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(PRICE_POINTS_MODE)
    String pricePointsMode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(DEPTH)
    Integer depth;

    public BitpandaProProductSubscription(
        String name, String[] instrumentCodes, BitpandaProWebsocketAuthData authData) {
      this.name = name;
      this.instrumentCodes = instrumentCodes;
    }

    public String getName() {
      return name;
    }

    public String[] getInstrumentCodes() {
      return instrumentCodes;
    }

    public void setPricePointsMode(String pricePointsMode) {
      this.pricePointsMode = pricePointsMode;
    }

    public void setDepth(Integer depth) {
      this.depth = depth;
    }
  }

  @JsonProperty(TYPE)
  private final String type;

  @JsonProperty(CHANNELS)
  private BitpandaProProductSubscription[] channels;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonProperty(SIGNATURE)
  String signature;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonProperty(KEY)
  String key;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonProperty(PASSPHRASE)
  String passphrase;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonProperty(TIMESTAMP)
  String timestamp;

  public BitpandaProWebSocketSubscriptionMessage(
      String type, ProductSubscription product, BitpandaProWebsocketAuthData authData) {
    this.type = type;
    generateSubscriptionMessage(product, authData);
  }

  public BitpandaProWebSocketSubscriptionMessage(
      String type, String[] channelNames, BitpandaProWebsocketAuthData authData) {
    this.type = type;
    generateSubscriptionMessage(channelNames, authData);
  }

  private String[] generateInstrumentCodes(CurrencyPair[] pairs) {
    List<String> productIds = new ArrayList<>(pairs.length);
    for (CurrencyPair pair : pairs) {
      productIds.add(pair.base.toString() + "_" + pair.counter.toString());
    }

    return productIds.toArray(new String[0]);
  }

  private BitpandaProProductSubscription generateBitpandaProInstruments(
      String name, CurrencyPair[] pairs, BitpandaProWebsocketAuthData authData) {
    String[] productsIds;
    productsIds = generateInstrumentCodes(pairs);
    return new BitpandaProProductSubscription(name, productsIds, authData);
  }

  private void generateSubscriptionMessage(
      String[] channelNames, BitpandaProWebsocketAuthData authData) {
    List<BitpandaProProductSubscription> channels = new ArrayList<>(3);
    for (String name : channelNames) {
      channels.add(new BitpandaProProductSubscription(name, null, authData));
    }
    this.channels = channels.toArray(new BitpandaProProductSubscription[0]);
  }

  private void generateSubscriptionMessage(
      ProductSubscription productSubscription, BitpandaProWebsocketAuthData authData) {
    List<BitpandaProProductSubscription> channels = new ArrayList<>(3);
    Map<String, List<CurrencyPair>> pairs = new HashMap<>(3);

    pairs.put(ORDER_BOOK, productSubscription.getOrderBook());
    pairs.put(MARKET_TICKER, productSubscription.getTicker());
    pairs.put(TRADES, productSubscription.getTrades());

    if (authData != null) {
      ArrayList<CurrencyPair> userCurrencies = new ArrayList<>();
      Stream.of(
              productSubscription.getUserTrades().stream(),
              productSubscription.getOrders().stream())
          .flatMap(s -> s)
          .distinct()
          .forEach(userCurrencies::add);
      pairs.put("user", userCurrencies);
    }

    for (Map.Entry<String, List<CurrencyPair>> product : pairs.entrySet()) {
      List<CurrencyPair> currencyPairs = product.getValue();
      if (currencyPairs == null || currencyPairs.size() == 0) {
        continue;
      }
      BitpandaProProductSubscription bitpandaProProductSubscription =
          generateBitpandaProInstruments(
              product.getKey(), product.getValue().toArray(new CurrencyPair[0]), authData);
      if (product.getKey().equals(ORDER_BOOK)) {
        bitpandaProProductSubscription.setDepth(BitpandaProSettings.ORDERBOOK_DEPTH);
      } else if (product.getKey().equals(MARKET_TICKER)) {
        bitpandaProProductSubscription.setPricePointsMode("OMIT");
      }

      channels.add(bitpandaProProductSubscription);
    }

    this.channels = channels.toArray(new BitpandaProProductSubscription[0]);

    if (authData != null) {
      this.key = authData.getKey();
      this.passphrase = authData.getPassphrase();
      this.signature = authData.getSignature();
      this.timestamp = String.valueOf(authData.getTimestamp());
    }
  }

  public String getType() {
    return type;
  }

  public BitpandaProProductSubscription[] getChannels() {
    return channels;
  }
}
