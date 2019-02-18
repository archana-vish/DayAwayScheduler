package com.deloitte.digital.Model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Model to store the final timetable
 * A Timetable has a start time, title and duration
 * Eg: 009:00 AM : Duck Herding 60min
 */
public class Timetable {
    String name;
    long duration;
    LocalTime startTime;

    static final String timeColonPattern = "hh:mm a";
    static final DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timeColonPattern);

    public Timetable(String name, LocalTime startTime, long duration) {
        this.name = name;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return timeColonFormatter.format(startTime) + " : " + name + " " + duration + "min";
    }
}
