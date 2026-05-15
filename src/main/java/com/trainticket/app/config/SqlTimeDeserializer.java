package com.trainticket.app.config;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;

public class SqlTimeDeserializer extends JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        LocalTime time = LocalTime.parse(value);
        return Time.valueOf(time);
    }
}
