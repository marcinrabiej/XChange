package info.bitrich.xchangestream.bitpandapro;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.service.netty.ConnectionStateModel.State;
import io.reactivex.Completable;
import io.reactivex.Observable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitpandapro.BitpandaProExchange;
import org.knowm.xchange.bitpandapro.dto.account.BitpandaProWebsocketAuthData;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;

public class BitpandaProStreamingExchange extends BitpandaProExchange implements StreamingExchange {
  private static final String API_URI = "wss://streams.exchange.bitpanda.com";

  private BitpandaProStreamingService streamingService;
  private BitpandaProStreamingMarketDataService streamingMarketDataService;
  private BitpandaProStreamingTradeService streamingTradeService;

  public BitpandaProStreamingExchange() {}

  @Override
  protected void initServices() {
    super.initServices();
  }

  @Override
  public Completable connect(ProductSubscription... args) {
    if (args == null || args.length == 0)
      throw new UnsupportedOperationException("The ProductSubscription must be defined!");
    ExchangeSpecification exchangeSpec = getExchangeSpecification();

    this.streamingService = new BitpandaProStreamingService(API_URI, () -> authData(exchangeSpec));
    applyStreamingSpecification(exchangeSpecification, this.streamingService);
    this.streamingTradeService = new BitpandaProStreamingTradeService(streamingService);
    this.streamingMarketDataService = new BitpandaProStreamingMarketDataService(streamingService);
    streamingService.setProductSubscription(args[0]);
    return streamingService.connect();
  }

  private BitpandaProWebsocketAuthData authData(ExchangeSpecification exchangeSpec) {
    BitpandaProWebsocketAuthData authData = null;
    /* if (exchangeSpec.getApiKey() != null) {
      try {
        CoinbaseProAccountServiceRaw rawAccountService =
            (CoinbaseProAccountServiceRaw) getAccountService();
        authData = rawAccountService.getWebsocketAuthData();
      } catch (Exception e) {
        logger.warn(
            "Failed attempting to acquire Websocket AuthData needed for private data on"
                + " websocket.  Will only receive public information via API",
            e);
      }
    }*/
    return authData;
  }

  @Override
  public Completable disconnect() {
    BitpandaProStreamingService service = streamingService;
    streamingService = null;
    streamingMarketDataService = null;
    return service.disconnect();
  }

  @Override
  public Observable<Throwable> reconnectFailure() {
    return streamingService.subscribeReconnectFailure();
  }

  @Override
  public Observable<Object> connectionSuccess() {
    return streamingService.subscribeConnectionSuccess();
  }

  @Override
  public Observable<State> connectionStateObservable() {
    return streamingService.subscribeConnectionState();
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification spec = super.getDefaultExchangeSpecification();
    spec.setShouldLoadRemoteMetaData(false);
    return spec;
  }

  @Override
  public BitpandaProStreamingMarketDataService getStreamingMarketDataService() {
    return streamingMarketDataService;
  }

  @Override
  public StreamingAccountService getStreamingAccountService() {
    throw new NotYetImplementedForExchangeException();
  }

  @Override
  public BitpandaProStreamingTradeService getStreamingTradeService() {
    return streamingTradeService;
  }

  @Override
  public boolean isAlive() {
    return streamingService != null && streamingService.isSocketOpen();
  }

  @Override
  public void useCompressedMessages(boolean compressedMessages) {
    streamingService.useCompressedMessages(compressedMessages);
  }
}
