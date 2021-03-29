package org.knowm.xchange.bitpandapro;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.meta.ExchangeMetaData;

public class BitpandaProExchangeIntegrationTest {

  @Test
  public void testCreateExchangeShouldApplyDefaultSpecification() {
    final Exchange exchange = ExchangeFactory.INSTANCE.createExchange(BitpandaProExchange.class);

    assertThat(exchange.getExchangeSpecification().getSslUri())
        .isEqualTo("https://api.exchange.bitpanda.com");
    assertThat(exchange.getExchangeSpecification().getHost())
        .isEqualTo("api.exchange.bitpanda.com");
  }

  @Test
  public void coinbaseShouldBeInstantiatedWithoutAnExceptionWhenUsingDefaultSpecification() {

    ExchangeFactory.INSTANCE.createExchange(BitpandaProExchange.class.getCanonicalName());
  }
  /*
  @Test
  public void shouldSupportEthUsdByRemoteInit() throws Exception {

    Exchange ex =
        ExchangeFactory.INSTANCE.createExchange(BitpandaProExchange.class.getCanonicalName());
    ex.remoteInit();

    CurrencyPair currencyPair = new CurrencyPair("ETH", "USD");
    Assert.assertTrue(
        ((BitpandaProMarketDataServiceRaw) ex.getMarketDataService())
            .checkProductExists(currencyPair));
  }*/

  @Test
  public void testExchangeMetaData() {
    final Exchange exchange = ExchangeFactory.INSTANCE.createExchange(BitpandaProExchange.class);

    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();

    Assert.assertNotNull(exchangeMetaData);
    Assert.assertNotNull(exchangeMetaData.getCurrencies());
    Assert.assertNotNull(
        "BTC is not defined", exchangeMetaData.getCurrencies().get(new Currency("BTC")));
    Assert.assertNotNull(exchangeMetaData.getCurrencyPairs());
    Assert.assertNotNull(
        "BTC_EUR is not defined",
        exchangeMetaData.getCurrencyPairs().get(new CurrencyPair("BTC/EUR")));
  }
}
