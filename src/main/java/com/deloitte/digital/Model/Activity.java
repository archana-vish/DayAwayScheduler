package com.deloitte.digital.Model;

/**
 * Model to store Activity
 * An activity has a title and a duration
 * Eg: 'Duck herding 60min' => Title: Duck herding Duration: 60l
 */

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
