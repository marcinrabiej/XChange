package org.knowm.xchange.bitpandapro.service;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bitpandapro.BitpandaProAdapters;
import org.knowm.xchange.bitpandapro.dto.BitpandaProException;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProCurrency;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProInstrument;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProOrderBook;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProTicker;
import org.knowm.xchange.currency.CurrencyPair;

@Slf4j
public class BitpandaProMarketDataServiceRaw extends BitpandaProBaseService {

  public BitpandaProMarketDataServiceRaw(Exchange exchange) {

    super(exchange);
  }

  public BitpandaProTicker getBitpandaProProductTicker(CurrencyPair currencyPair)
      throws IOException {
    try {
      return bitpandaPro.getMarketTicker(BitpandaProAdapters.toInstrumentCode(currencyPair));
    } catch (BitpandaProException e) {
      throw handleError(e);
    }
  }

  public List<BitpandaProCurrency> getBitpandaProCurrencies() throws IOException {
    try {
      return bitpandaPro.getCurrencies();
    } catch (BitpandaProException e) {
      throw handleError(e);
    }
  }

  public List<BitpandaProInstrument> getBitpandaProInstruments() throws IOException {
    try {
      return bitpandaPro.getInstruments();
    } catch (BitpandaProException e) {
      throw handleError(e);
    }
  }

  public BitpandaProOrderBook getBitpandaProOrderBook(
      CurrencyPair currencyPair, int level, int depth) throws IOException {

    try {
      return bitpandaPro.getOrderBook(
          BitpandaProAdapters.toInstrumentCode(currencyPair),
          String.valueOf(level),
          String.valueOf(depth));
    } catch (BitpandaProException e) {
      throw handleError(e);
    }
  }
}
