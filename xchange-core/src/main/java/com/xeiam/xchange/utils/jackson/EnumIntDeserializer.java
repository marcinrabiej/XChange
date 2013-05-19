package com.xeiam.xchange.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @author Matija Mazi <br/>
 *         0-based enum deserializer. This wil deserialize 0 as the first enum constant, 1 as the second etc.
 */
public abstract class EnumIntDeserializer<E extends Enum<E>> extends JsonDeserializer<E> {

  private Class<E> enumClass;

  protected EnumIntDeserializer(Class<E> enumClass) {

    this.enumClass = enumClass;
  }

  @Override
  public E deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

    E[] constants = enumClass.getEnumConstants();
    return constants[jp.getValueAsInt() + getIndexBase()];
  }

  protected int getIndexBase() {

    return 0;
  }
}
