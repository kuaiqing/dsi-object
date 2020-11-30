package com.techstar.om.dasi.jpa.convert;

import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;

public class DateTimeConverter implements AttributeConverter<DateTime, Timestamp> {
    public static final DateTimeConverter instance = new DateTimeConverter();

    @Override
    public Timestamp convertToDatabaseColumn(DateTime attribute) {
        return null != attribute ? new Timestamp(attribute.getMillis()) : null;
    }

    @Override
    public DateTime convertToEntityAttribute(Timestamp dbData) {
        return null != dbData ? new DateTime(dbData.getTime()) : null;
    }
}
