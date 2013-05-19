/**
 * Copyright (C) 2012 - 2013 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.oer;

import com.xeiam.xchange.currency.MoneyUtils;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Ticker.TickerBuilder;
import org.joda.money.BigMoney;

import java.util.Date;

/**
 * Various adapters for converting from OER DTOs to XChange DTOs
 */
public final class OERAdapters {

  /**
   * private Constructor
   */
  private OERAdapters() {

  }

  public static Ticker adaptTicker(String tradableIdentifier, Double exchangeRate, Long timestamp) {

    BigMoney last = MoneyUtils.parse(tradableIdentifier + " " + exchangeRate);
    Date timestampDate = new Date(timestamp);
    return TickerBuilder.newInstance().withTradableIdentifier(tradableIdentifier).withLast(last).withTimestamp(timestampDate).build();
  }

}
