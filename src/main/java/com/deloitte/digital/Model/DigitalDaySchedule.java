package com.deloitte.digital.Model;

import com.deloitte.digital.Exceptions.InvalidScheduleException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DigitalDaySchedule implements Schedule {

    List<Activity> digitalDayActivities;
    long totalActivitiesDuration;
    int teamSize;

    Logger logger = Logger.getLogger(DigitalDaySchedule.class.getName());


    public void setTotalActivitiesDuration() throws InvalidScheduleException {
        if (this.digitalDayActivities.size() == 0) {
            throw new InvalidScheduleException("Empty activity list.");
        } else {
            this.totalActivitiesDuration = digitalDayActivities.stream()
                    .map(Activity::getDuration)
                    .reduce(0l, Long::sum);

            if (this.totalActivitiesDuration < getMinimumTotalActivityTimePerDay()) {
                throw new InvalidScheduleException("Activity timings not enough to fill the Digital Day Schedule");
            }
        }
    }

    public void setTeamSize() throws InvalidScheduleException {
        if (this.totalActivitiesDuration == 0) {
            throw new InvalidScheduleException("Schedule timing error");
        } else {
            teamSize =  Math.toIntExact(totalActivitiesDuration / getMinimumTotalActivityTimePerDay());
        } if (teamSize == 0) {
            throw new InvalidScheduleException("Insufficient activities to form teams");
        }
        logger.info("Team size set to :: " + teamSize);
    }

    public void setDigitalDayActivities(List<Activity> digitalDayActivities) {
        this.digitalDayActivities = new ArrayList<>(digitalDayActivities);
    }

    public List<Activity> getDigitalDayActivities() {
        return digitalDayActivities;
    }

    public long getTeamSize() {
        return teamSize;
    }
}
