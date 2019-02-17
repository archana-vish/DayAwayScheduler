package com.deloitte.digital.Model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public  class Activity {

    private String title;
    private long duration;

    public Activity(String title, long duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                '}';
    }
}
