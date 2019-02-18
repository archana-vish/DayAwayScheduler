package com.deloitte.digital.Model;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


/**
 * Interface to hold information on a Schedule
 * Every schedule has the following
 * START_OF_DAY     => Indicates the start hour eg: 09:00
 * LUNCH_START      => Indicates lunch start time eg: 12:00
 * LUNCH_END        => Indicates lunch end time eg: 13:00
 * MIN_END_OF_DAY   => Indicates the minimum time a day's schedule can end eg: 16:00
 * MAX_END_OF_DAY   => Indicates the maximum time a day's schedule can end eg: 17:00
 * SPRINT           => When an activity is in 'Sprints' eg: A Sprint in this case is 15min
 * UNIT             => Indicates the time unit
 */

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

    /**
     * Method to get the Pre-lunch duration
     * @return duration in long
     */
    default long getPreLunchDuration() {
        return UNIT.between(START_OF_DAY, LUNCH_START);
    }

    /**
     * Method to get Post-lunch duration
     * This is up to the maximum time a day's schedule can end
     * @return duration in long
     */
    default long getPostLunchMaxDuration() {
        return UNIT.between(LUNCH_END, MAX_END_OF_DAY);
    }

    /**
     * Method to get Post-lunch minimum duration
     * This is up to the minimum time a day's schedule can end
     * @return duration in long
     */
    default long getPostLunchMinDuration() {
        return UNIT.between(LUNCH_END, MIN_END_OF_DAY);
    }

    /**
     * Method to get the minimum total activity time per day
     * @return duration in long
     */
    default long getMinimumTotalActivityTimePerDay()  {
        return (getPreLunchDuration() + getPostLunchMinDuration());
    }

}
