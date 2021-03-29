package info.bitrich.xchangestream.bitpandapro;

import com.fasterxml.jackson.databind.JsonNode;
import info.bitrich.xchangestream.bitpandapro.dto.BitpandaProWebSocketSubscriptionMessage;
import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import io.reactivex.Observable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import org.knowm.xchange.bitpandapro.dto.account.BitpandaProWebsocketAuthData;

public class BitpandaProStreamingService extends JsonNettyStreamingService {
  private static final String SUBSCRIBE = "subscribe";
  private static final String UNSUBSCRIBE = "unsubscribe";
  private static final String SHARED_CHANNEL_NAME = "ALL";
  private final Supplier<BitpandaProWebsocketAuthData> authData;
  private ProductSubscription productSubscription;
  private final Map<String, Observable<JsonNode>> subscriptions = new ConcurrentHashMap<>();

  public BitpandaProStreamingService(
      String apiUrl, Supplier<BitpandaProWebsocketAuthData> authData) {
    super(apiUrl, Integer.MAX_VALUE);
    this.authData = authData;
  }

  @Override
  public String getSubscriptionUniqueId(String channelName, Object... args) {
    return SHARED_CHANNEL_NAME;
  }

  /**
   * Subscribes to the provided channel name, maintains a cache of subscriptions, in order not to
   * subscribe more than once to the same channel.
   *
   * @param channelName the name of the requested channel.
   * @return an Observable of json objects coming from the exchange.
   */
  @Override
  public Observable<JsonNode> subscribeChannel(String channelName, Object... args) {
    channelName = SHARED_CHANNEL_NAME;

    if (!channels.containsKey(channelName) && !subscriptions.containsKey(channelName)) {
      subscriptions.put(channelName, super.subscribeChannel(channelName, args));
    }

    return subscriptions.get(channelName);
  }

  @Override
  protected String getChannelNameFromMessage(JsonNode message) {
    return SHARED_CHANNEL_NAME;
  }

  @Override
  public String getSubscribeMessage(String channelName, Object... args) throws IOException {
    BitpandaProWebSocketSubscriptionMessage subscribeMessage =
        new BitpandaProWebSocketSubscriptionMessage(SUBSCRIBE, productSubscription, authData.get());
    return objectMapper.writeValueAsString(subscribeMessage);
  }

  @Override
  public String getUnsubscribeMessage(String channelName) throws IOException {
    BitpandaProWebSocketSubscriptionMessage subscribeMessage =
        new BitpandaProWebSocketSubscriptionMessage(
            UNSUBSCRIBE,
            new String[] {
              "ORDER_BOOK",
              "PRICE_TICKS",
              "CANDLESTICKS",
              "MARKET_TICKER",
              "ACCOUNT_HISTORY",
              "ORDERS",
              "TRADING"
            },
            authData.get());
    return objectMapper.writeValueAsString(subscribeMessage);
  }

  @Override
  protected void handleChannelMessage(String channel, JsonNode message) {
    if (SHARED_CHANNEL_NAME.equals(channel)) {
      channels.forEach((k, v) -> v.getEmitter().onNext(message));

    } else {
      super.handleChannelMessage(channel, message);
    }
  }

  public void setProductSubscription(ProductSubscription productSubscription) {
    this.productSubscription = productSubscription;
  }

  public ProductSubscription getProductSubscription() {
    return productSubscription;
  }
}
