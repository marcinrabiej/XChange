package info.bitrich.xchangestream.bitpandapro.dto.marketdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class ModifiableOrdersMap {
  private TreeMap<BigDecimal, OrderBookEntry> ordersMapByPrice;
  private int maxDepth;

  public ModifiableOrdersMap(
      List<OrderBookEntry> orderBookEntries, Comparator<BigDecimal> comparator, int maxDepth) {
    ordersMapByPrice = new TreeMap<>(comparator);
    this.maxDepth = maxDepth;
    orderBookEntries.forEach(o -> ordersMapByPrice.put(o.getPrice(), o));
    truncate();
  }

  public void truncate() {
    while (ordersMapByPrice.size() > maxDepth) {
      ordersMapByPrice.remove(ordersMapByPrice.lastKey());
    }
  }

  public List<OrderBookEntry> getOrderedOrdersList() {
    return new ArrayList<>(ordersMapByPrice.values());
  }

  public void applyUpdate(OrderBookEntry update) {
    ordersMapByPrice.remove(update.getPrice());
    if (!update.getVolume().stripTrailingZeros().equals(BigDecimal.ZERO)) {
      ordersMapByPrice.put(update.getPrice(), update);
    }
  }
}
