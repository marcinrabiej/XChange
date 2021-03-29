package info.bitrich.xchangestream.bitpandapro;

import info.bitrich.xchangestream.core.StreamingTradeService;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.UserTrade;

public class BitpandaProStreamingTradeService implements StreamingTradeService {
  private BitpandaProStreamingService streamingService;

  public BitpandaProStreamingTradeService(BitpandaProStreamingService streamingService) {

    this.streamingService = streamingService;
  }

  @Override
  public Observable<Order> getOrderChanges(CurrencyPair currencyPair, Object... args) {
    return null;
  }

  @Override
  public Observable<UserTrade> getUserTrades(CurrencyPair currencyPair, Object... args) {
    return null;
  }
}
