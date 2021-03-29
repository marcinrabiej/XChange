package org.knowm.xchange.bitpandapro.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.bitpandapro.BitpandaPro;
import org.knowm.xchange.bitpandapro.dto.BitpandaProException;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.FundsExceededException;
import org.knowm.xchange.exceptions.InternalServerException;
import org.knowm.xchange.exceptions.RateLimitExceededException;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import si.mazi.rescu.ParamsDigest;

public class BitpandaProBaseService extends BaseExchangeService implements BaseService {

  protected final BitpandaPro bitpandaPro;
  protected final ParamsDigest digest;

  protected final String apiKey;
  protected final String passphrase;

  protected BitpandaProBaseService(Exchange exchange) {

    super(exchange);
    bitpandaPro =
        ExchangeRestProxyBuilder.forInterface(
                BitpandaPro.class, exchange.getExchangeSpecification())
            .build();
    digest = BitpandaProDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
    apiKey = exchange.getExchangeSpecification().getApiKey();
    passphrase =
        (String)
            exchange.getExchangeSpecification().getExchangeSpecificParametersItem("passphrase");
  }

  protected ExchangeException handleError(BitpandaProException exception) {
    if (exception.getMessage() != null) {
      if (exception.getMessage().contains("Insufficient")) {
        return new FundsExceededException(exception);
      } else if (exception.getMessage().contains("Rate limit exceeded")) {
        return new RateLimitExceededException(exception);
      } else if (exception.getMessage().contains("Internal server error")) {
        return new InternalServerException(exception);
      }
    }
    return new ExchangeException(exception);
  }
}
