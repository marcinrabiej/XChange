package org.knowm.xchange.bitpandapro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import si.mazi.rescu.HttpStatusExceptionSupport;

public class BitpandaProException extends HttpStatusExceptionSupport {

  private final String message;

  public BitpandaProException(@JsonProperty("message") String message) {
    super(message);
    this.message = message;
  }

  public BitpandaProException(Exception e) {
    this.message = e.getMessage();
  }

  @Override
  public String getMessage() {
    return message;
  }
}
