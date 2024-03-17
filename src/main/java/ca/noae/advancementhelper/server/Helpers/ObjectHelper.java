package ca.noae.advancementhelper.server.Helpers;

import ca.noae.advancementhelper.server.Structures.AHRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public final class ObjectHelper {

  static ObjectMapper mapper;

  public ObjectHelper(ObjectMapper mapper) { ObjectHelper.mapper = mapper; }

  public static String convertToString(final AHRequest object)
      throws JsonProcessingException {
    return mapper.writeValueAsString(object);
  }

  public static AHRequest convertFrom(final String objectAsString)
      throws StreamReadException, DatabindException, IOException {
    return mapper.readValue(objectAsString, AHRequest.class);
  }
}
