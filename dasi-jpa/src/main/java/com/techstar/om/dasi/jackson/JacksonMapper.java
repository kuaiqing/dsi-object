package com.techstar.om.dasi.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.util.TimeZone;

public enum JacksonMapper {
    instance;

    public final ObjectMapper objectMapper = new ObjectMapper();

    JacksonMapper() {
        objectMapper.registerModule(new JodaModule());
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        objectMapper.enable(MapperFeature.USE_GETTERS_AS_SETTERS);
        objectMapper.enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
