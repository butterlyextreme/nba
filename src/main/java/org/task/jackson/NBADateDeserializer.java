package org.task.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class NBADateDeserializer extends StdDeserializer<Date> {

  public NBADateDeserializer() {
    super(String.class);
  }

  @Override
  public Date deserialize(final JsonParser parser, final DeserializationContext context)
      throws IOException {

    String input = parser.getValueAsString();

    DateTimeFormatter patternOne = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
    DateTimeFormatter patternTwo = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    try {
      LocalDateTime datetime = LocalDateTime.parse(input, patternOne);
      return getDate(datetime);
    } catch (Exception e) {
      LocalDateTime datetime = LocalDateTime.parse(input, patternTwo);
      return getDate(datetime);
    }
  }

  private Date getDate(LocalDateTime datetime) {
    return java.sql.Date.valueOf(datetime.toLocalDate());
  }
}