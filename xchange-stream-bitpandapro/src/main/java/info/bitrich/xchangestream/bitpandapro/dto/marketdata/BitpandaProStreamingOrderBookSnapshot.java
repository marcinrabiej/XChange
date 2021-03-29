package info.bitrich.xchangestream.bitpandapro.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import info.bitrich.xchangestream.bitpandapro.BitpandaProStreamingAdapters;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.knowm.xchange.bitpandapro.dto.BitpandaProException;

@Data
public class BitpandaProStreamingOrderBookSnapshot {

  private final String instrumentCode;
  private final Date time;
  private final List<OrderBookEntry> bids;
  private final List<OrderBookEntry> asks;

  private BitpandaProStreamingOrderBookSnapshot(
      @JsonProperty("instrument_code") String instrumentCode,
      @JsonProperty("time") Date time,
      @JsonProperty("bids") Object[] rawbids,
      @JsonProperty("asks") Object[] rawasks) {

    this.instrumentCode = instrumentCode;
    this.time = time;
    this.bids = convertRawOrders(rawbids);
    this.asks = convertRawOrders(rawasks);
  }

  private List<OrderBookEntry> convertRawOrders(@JsonProperty("bids") Object[] rawbids) {
    return Arrays.stream(rawbids)
        .map(o -> (List<String>) o)
        .map(
            list ->
                new OrderBookEntry(
                    BitpandaProStreamingAdapters.getBigDecimalFromString(list.get(0)),
                    BitpandaProStreamingAdapters.getBigDecimalFromString(list.get(1))))
        .collect(Collectors.toList());
  }

  public static BitpandaProStreamingOrderBookSnapshot fromJsonNode(JsonNode jsonNode) {
    try {
      return StreamingObjectMapperHelper.getObjectMapper()
          .treeToValue(jsonNode, BitpandaProStreamingOrderBookSnapshot.class);
    } catch (JsonProcessingException e) {
      throw new BitpandaProException(e);
    }
  }
}
