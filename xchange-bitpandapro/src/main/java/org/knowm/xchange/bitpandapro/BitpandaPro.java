package org.knowm.xchange.bitpandapro;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.knowm.xchange.bitpandapro.dto.BitpandaProException;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProCurrency;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProInstrument;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProOrderBook;
import org.knowm.xchange.bitpandapro.dto.marketdata.BitpandaProTicker;

@Path("/public/v1/")
@Produces(MediaType.APPLICATION_JSON)
public interface BitpandaPro {

  @GET
  @Path("order-book/{instrumentCode}?level={level}&depth={depth}")
  BitpandaProOrderBook getOrderBook(
      @PathParam("instrumentCode") String instrumentCode,
      @PathParam("level") String level,
      @PathParam("depth") String depth)
      throws BitpandaProException, IOException;

  @GET
  @Path("market-ticker")
  BitpandaProTicker getMarketTicker() throws BitpandaProException, IOException;

  @GET
  @Path("currencies")
  List<BitpandaProCurrency> getCurrencies() throws BitpandaProException, IOException;

  @GET
  @Path("instruments")
  List<BitpandaProInstrument> getInstruments() throws BitpandaProException, IOException;

  @GET
  @Path("market-ticker/{instrumentCode}")
  BitpandaProTicker getMarketTicker(@PathParam("instrumentCode") String instrumentCode)
      throws BitpandaProException, IOException;
}
