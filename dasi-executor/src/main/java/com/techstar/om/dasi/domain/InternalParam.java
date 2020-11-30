package com.techstar.om.dasi.domain;

import org.joda.time.DateTime;

public enum InternalParam {
    Date,
    Datetime0,
    Datetime1,
    Datetime5,
    Datetime15,
    Datetime30,

    Time0,
    Time1,
    Time5,
    Time15,
    Time30;

    public String val() {
        DateTime now = DateTime.now();
        int minute = now.getMinuteOfHour();
        switch (this) {
            case Date:
                return now.toString("yyyy-MM-dd");
            case Datetime0:
                return now.toString("yyyy-MM-dd HH:00:00");
            case Datetime1:
                return now.toString("yyyy-MM-dd HH:mm:00");
            case Datetime5:
                return now.withMinuteOfHour(minute / 5 * 5).toString("yyyy-MM-dd HH:mm:00");
            case Datetime15:
                return now.withMinuteOfHour(minute / 15 * 15).toString("yyyy-MM-dd HH:mm:00");
            case Datetime30:
                return now.withMinuteOfHour(minute / 30 * 30).toString("yyyy-MM-dd HH:mm:00");

            case Time0:
                return now.toString("HH:00:00");
            case Time1:
                return now.toString("HH:mm:00");
            case Time5:
                return now.withMinuteOfHour(minute / 5 * 5).toString("HH:mm:00");
            case Time15:
                return now.withMinuteOfHour(minute / 15 * 15).toString("HH:mm:00");
            case Time30:
                return now.withMinuteOfHour(minute / 30 * 30).toString("HH:mm:00");
        }
        return "";
    }
}
