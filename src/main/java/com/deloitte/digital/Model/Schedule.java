package com.deloitte.digital.Model;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public interface Schedule {

    LocalTime START_OF_DAY = LocalTime.of(9, 0);

    LocalTime MIN_END_OF_DAY = LocalTime.of(16,0);
    LocalTime MAX_END_OF_DAY = LocalTime.of(17,0);

    LocalTime LUNCH_START  = LocalTime.of(12,0);
    LocalTime LUNCH_END    = LocalTime.of(13,0);

    String SPRINT = "15min";
    ChronoUnit UNIT = ChronoUnit.MINUTES;

    String MANDATORY_END_ACTIVITY = "Staff Motivation Presentation";
    long MANDATORY_END_ACTIVITY_DURATION = 60l;

    default long getPreLunchDuration() {
        return UNIT.between(START_OF_DAY, LUNCH_START);
    }

    default long getPostLunchMaxDuration() {
        return UNIT.between(LUNCH_END, MAX_END_OF_DAY);
    }

    default long getPostLunchMinDuration() {
        return UNIT.between(LUNCH_END, MIN_END_OF_DAY);
    }

    default long getMinimumTotalActivityTimePerDay()  {
        return (getPreLunchDuration() + getPostLunchMinDuration());
    }

}
