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
public class BitpandaProStreamingOrderBookUpdate {

  private final String instrumentCode;
  private final Date time;
  private final List<OrderBookEntry> askUpdates;
  private final List<OrderBookEntry> bidUpdates;

  private BitpandaProStreamingOrderBookUpdate(
      @JsonProperty("instrument_code") String instrumentCode,
      @JsonProperty("time") Date time,
      @JsonProperty("changes") Object[] changes) {

    this.instrumentCode = instrumentCode;
    this.time = time;
    askUpdates = convertChanges(changes, "SELL");
    bidUpdates = convertChanges(changes, "BUY");
  }

  private List<OrderBookEntry> convertChanges(
      @JsonProperty("changes") Object[] changes, String side) {
    return Arrays.stream(changes)
        .map(o -> (List<String>) o)
        .filter(list -> list.get(0).equals(side))
        .map(
            list ->
                new OrderBookEntry(
                    BitpandaProStreamingAdapters.getBigDecimalFromString(list.get(1)),
                    BitpandaProStreamingAdapters.getBigDecimalFromString(list.get(2))))
        .collect(Collectors.toList());
  }

  public static BitpandaProStreamingOrderBookUpdate fromJsonNode(JsonNode jsonNode) {
    try {
      return StreamingObjectMapperHelper.getObjectMapper()
          .treeToValue(jsonNode, BitpandaProStreamingOrderBookUpdate.class);
    } catch (JsonProcessingException e) {
      throw new BitpandaProException(e);
    }
  }
}
