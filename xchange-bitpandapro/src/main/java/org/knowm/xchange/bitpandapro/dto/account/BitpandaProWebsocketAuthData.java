package org.knowm.xchange.bitpandapro.dto.account;

public class BitpandaProWebsocketAuthData {
  private final String userId;
  private final String key;
  private final String passphrase;
  private final String signature;
  private final long timestamp;

  public BitpandaProWebsocketAuthData(
      String userId, String key, String passphrase, String signature, long timestamp) {
    this.userId = userId;
    this.key = key;
    this.passphrase = passphrase;
    this.signature = signature;
    this.timestamp = timestamp;
  }

  public String getUserId() {
    return userId;
  }

  public String getKey() {
    return key;
  }

  public String getPassphrase() {
    return passphrase;
  }

  public String getSignature() {
    return signature;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
