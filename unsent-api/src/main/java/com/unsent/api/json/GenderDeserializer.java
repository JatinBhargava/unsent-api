package com.unsent.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.unsent.util.Gender;

import java.io.IOException;

public class GenderDeserializer extends JsonDeserializer<Gender> {

    @Override
    public Gender deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return Gender.fromValue(parser.getValueAsString());
    }
}
