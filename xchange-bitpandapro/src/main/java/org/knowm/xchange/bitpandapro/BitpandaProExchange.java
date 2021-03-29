package org.knowm.xchange.bitpandapro;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProCurrency;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProInstrument;
import org.knowm.xchange.bitpandapro.service.BitpandaProMarketDataService;
import org.knowm.xchange.bitpandapro.service.BitpandaProMarketDataServiceRaw;
import org.knowm.xchange.utils.nonce.CurrentTimeNonceFactory;
import si.mazi.rescu.SynchronizedValueFactory;

public class BitpandaProExchange extends BaseExchange {

  private SynchronizedValueFactory<Long> nonceFactory = new CurrentTimeNonceFactory();

  @Override
  protected void initServices() {
    this.marketDataService = new BitpandaProMarketDataService(this);
    this.accountService = null;
    this.tradeService = null;
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.exchange.bitpanda.com");
    exchangeSpecification.setHost("api.exchange.bitpanda.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("BitpandaPro");
    exchangeSpecification.setExchangeDescription("BitpandaPro Exchange");
    return exchangeSpecification;
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {

    return nonceFactory;
  }

  @Override
  public void remoteInit() throws IOException {
    List<BitpandaProInstrument> instruments =
        ((BitpandaProMarketDataServiceRaw) marketDataService).getBitpandaProInstruments();
    List<BitpandaProCurrency> currencies =
        ((BitpandaProMarketDataServiceRaw) marketDataService).getBitpandaProCurrencies();
    exchangeMetaData =
        BitpandaProAdapters.adaptToExchangeMetaData(exchangeMetaData, instruments, currencies);
  }
}
