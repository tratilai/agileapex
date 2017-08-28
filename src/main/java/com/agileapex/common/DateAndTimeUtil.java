package com.agileapex.common;

import java.sql.Timestamp;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.vaadin.ui.AbstractField;

public class DateAndTimeUtil {

    private final DateTimeFormatter mediumDateTimeFormatter = DateTimeFormat.mediumDateTime();
    private final DateTimeFormatter mediumDateFormatter = DateTimeFormat.mediumDate();
    private final DateTimeFormatter shortDateFormatter = DateTimeFormat.shortDate();
    private final DateTimeFormatter dateAndMonthShortFormatter = DateTimeFormat.forPattern("d.M.");
    
    public String formatToMediumDateAndTime(LocalDate date) {
        return mediumDateTimeFormatter.print(date);
    }

    public String formatToMediumDateAndTime(DateTime date) {
        return mediumDateTimeFormatter.print(date);
    }

    public String formatToMediumDate(DateTime date) {
        return mediumDateFormatter.print(date);
    }

    public String formatToMediumDate(LocalDate date) {
        return mediumDateFormatter.print(date);
    }

    public String formatToShortDate(DateTime date) {
        return shortDateFormatter.print(date);
    }

    public String formatToShortDate(LocalDate date) {
        return shortDateFormatter.print(date);
    }

    public String formatToOnlyDateAndMonthShort(LocalDate date) {
        return dateAndMonthShortFormatter.print(date);
    }

    public DateTime convert(AbstractField field) {
        Date date = (Date) field.getValue();
        return new DateTime(date);
    }

    public DateTime convert(Timestamp timestamp) {
        DateTime dateTime = null;
        if (timestamp != null) {
            dateTime = new DateTime(timestamp.getTime());
        }
        return dateTime;
    }
}
