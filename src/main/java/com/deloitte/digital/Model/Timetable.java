package com.deloitte.digital.Model;

import java.time.LocalTime;

public class Timetable {
    String name;
    LocalTime startTime;

    public Timetable(String name, LocalTime startTime) {
        this.name = name;
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "" +
                "Activity :: '" + name + '\'' +
                ", Start Time :: " + startTime ;
    }
}
