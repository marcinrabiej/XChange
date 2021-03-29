package info.bitrich.xchangestream.bitpandapro.dto.marketdata;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class ModifiableOrderBook {
  private Date time;
  private ModifiableOrdersMap bids;
  private ModifiableOrdersMap asks;

  public ModifiableOrderBook(
      BitpandaProStreamingOrderBookSnapshot bitpandaProStreamingOrderBookSnapshot, int maxDepth) {
    this.time = bitpandaProStreamingOrderBookSnapshot.getTime();
    this.bids =
        new ModifiableOrdersMap(
            bitpandaProStreamingOrderBookSnapshot.getBids(),
            Collections.reverseOrder(BigDecimal::compareTo),
            maxDepth);
    this.asks =
        new ModifiableOrdersMap(
            bitpandaProStreamingOrderBookSnapshot.getAsks(), BigDecimal::compareTo, maxDepth);
  }

  public void updateOrderBook(
      BitpandaProStreamingOrderBookUpdate bitpandaProStreamingOrderBookUpdate) {
    this.time = bitpandaProStreamingOrderBookUpdate.getTime();
    updateOrders(bids, bitpandaProStreamingOrderBookUpdate.getBidUpdates());
    updateOrders(asks, bitpandaProStreamingOrderBookUpdate.getAskUpdates());
  }

  private void updateOrders(ModifiableOrdersMap orders, List<OrderBookEntry> orderUpdates) {
    orderUpdates.forEach(orders::applyUpdate);
    orders.truncate();
  }
}
