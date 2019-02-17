package com.deloitte.digital.Model;

import com.deloitte.digital.Exceptions.InvalidScheduleException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DigitalDaySchedule implements Schedule {

    List<Activity> digitalDayActivities;
    List<Activity> masterList;
    long totalActivitiesDuration;
    long teamSize;

    Logger logger = Logger.getLogger(DigitalDaySchedule.class.getName());


    public void setTotalActivitiesDuration() throws InvalidScheduleException {
        if (this.digitalDayActivities.size() == 0) {
            throw new InvalidScheduleException("No activities in the list of schedule");
        } else {
            this.totalActivitiesDuration = digitalDayActivities.stream()
                    .map(Activity::getDuration)
                    .reduce(0l, Long::sum);
        }
    }

    public void setTeamSize() throws InvalidScheduleException {
        if (this.totalActivitiesDuration == 0) {
            throw new InvalidScheduleException("Schedule timing error");
        } else {
            teamSize =  totalActivitiesDuration / getMinimumTotalActivityTimePerDay();
        }
        logger.info("Team size set to :: " + teamSize);
    }

    public void setDigitalDayActivities(List<Activity> digitalDayActivities) {
        this.digitalDayActivities = new ArrayList<>(digitalDayActivities);
        this.masterList = new ArrayList<>(digitalDayActivities);
    }

    public List<Activity> getDigitalDayActivities() {
        return digitalDayActivities;
    }

    public long getTeamSize() {
        return teamSize;
    }

    public void resetList() {
        digitalDayActivities = new ArrayList<>(this.masterList);
    }
}
