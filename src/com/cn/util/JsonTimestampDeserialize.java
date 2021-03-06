package com.cn.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by SNNU on 2015/5/9.
 */
public class JsonTimestampDeserialize extends JsonDeserializer<Timestamp> {
    @Override
    public Timestamp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText().trim();
        if (text.indexOf(':') < 0) {
            text += " 00:00:00";
        }
        return Timestamp.valueOf(text);
    }
}
