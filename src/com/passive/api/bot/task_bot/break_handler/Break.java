package com.passive.api.bot.task_bot.break_handler;

import com.passive.api.util.Formatting;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Created by SlashnHax on 20/12/2015.
 */
public class Break {
    private long startTime;
    private long length;
    private long endTime;

    private StringPropertyBase startTimeString = new SimpleStringProperty();
    private StringPropertyBase lengthString = new SimpleStringProperty();
    private StringPropertyBase endTimeString = new SimpleStringProperty();

    public Break(long startTime, long length) {
        this.startTime = startTime;
        this.length = length;
        this.endTime = startTime + length;
        startTimeString.set(Formatting.formatAsTime(startTime));
        lengthString.set(Formatting.formatAsTime(length));
        endTimeString.set(Formatting.formatAsTime(endTime));

        startTimeString.addListener((observable, oldValue, newValue) -> {
            if (!Formatting.TIME_PATTERN.matcher(newValue).matches()) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Invalid time value. Please enter in the format \"00:00:00\"", ButtonType.OK);
                a.show();
                startTimeString.set(oldValue);
            } else {
                setStartTime(Formatting.timeStringAsMillis(newValue));
            }
        });
        lengthString.addListener((observable, oldValue, newValue) -> {
            if (!Formatting.TIME_PATTERN.matcher(newValue).matches()) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Invalid time value. Please enter in the format \"00:00:00\"", ButtonType.OK);
                a.show();
                lengthString.set(oldValue);
            } else {
                setLength(Formatting.timeStringAsMillis(newValue));
            }
        });
        endTimeString.addListener((observable, oldValue, newValue) -> {
            if (!Formatting.TIME_PATTERN.matcher(newValue).matches()) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Invalid time value. Please enter in the format \"00:00:00\"", ButtonType.OK);
                a.show();
                endTimeString.set(oldValue);
            } else {
                setEndTime(Formatting.timeStringAsMillis(newValue));
            }
        });
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        this.length = endTime - startTime;
        startTimeString.set(Formatting.formatAsTime(startTime));
        lengthString.set(Formatting.formatAsTime(length));
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
        this.endTime = startTime + length;
        lengthString.set(Formatting.formatAsTime(length));
        endTimeString.set(Formatting.formatAsTime(endTime));
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        this.length = endTime - startTime;
        lengthString.set(Formatting.formatAsTime(length));
        endTimeString.set(Formatting.formatAsTime(endTime));
    }

    public boolean isActive(long currentTime) {
        return startTime < currentTime && currentTime < endTime;
    }

    public long getRemainingLength(long currentTime) {
        return endTime - currentTime;
    }

    public boolean isExpired(long currentTime) {
        return startTime != -1 && endTime < currentTime;
    }

    public StringProperty getStartTimeString() {
        return startTimeString;
    }

    public StringProperty getEndTimeString() {
        return endTimeString;
    }

    public StringProperty getLengthString() {
        return lengthString;
    }

    public String toString() {
        return startTimeString.get() + " - " + endTimeString.get() + " Length: " + lengthString.get();
    }
}
