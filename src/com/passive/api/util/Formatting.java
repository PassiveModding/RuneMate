package com.passive.api.util;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by Taffy on 14/12/2015.
 */
public class Formatting {
    public static final Pattern TIME_PATTERN = Pattern.compile("^\\d+:\\d+:\\d+$");
    private static final NumberFormat LOCAL = DecimalFormat.getInstance();
    private static final DecimalFormat DOUBLE_DIGITS = new DecimalFormat("00");
    private static final DecimalFormat TWO_DEC_PL = new DecimalFormat("00.00");
    private static StringConverter<Number> numberConverter = new NumberStringConverter();

    public static String format(long l) {
        return LOCAL.format(l);
    }

    public static String format(double d) {
        return LOCAL.format(d);
    }

    public static String formatAsTime(long time) {
        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        return (days > 0 ? DOUBLE_DIGITS.format(days) + ":" : "")
                + DOUBLE_DIGITS.format(hours) + ":"
                + DOUBLE_DIGITS.format(minutes) + ":"
                + DOUBLE_DIGITS.format(seconds);
    }

    public static long timeStringAsMillis(String timeString) {
        String[] split = timeString.split(":");
        return TimeUnit.HOURS.toMillis(Integer.parseInt(split[0]))
                + TimeUnit.MINUTES.toMillis(Integer.parseInt(split[1]))
                + TimeUnit.SECONDS.toMillis(Integer.parseInt(split[2]));
    }

    public static StringConverter<Number> getTimeStringConverter() {
        return new StringConverter<Number>() {
            private Number lastNumber;

            @Override
            public String toString(Number object) {
                return formatAsTime(object.longValue());
            }

            @Override
            public Number fromString(String string) {
                if (TIME_PATTERN.matcher(string).matches())
                    lastNumber = timeStringAsMillis(string);
                return lastNumber;
            }
        };
    }

    public static StringProperty createFormattedNumberProperty(Property<Number> number) {
        StringProperty result = new SimpleStringProperty();
        result.bindBidirectional(number, numberConverter);
        return result;
    }

    public static String twoDecPl(double d) {
        return TWO_DEC_PL.format(d);
    }
}
