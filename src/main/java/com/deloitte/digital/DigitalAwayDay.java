package com.deloitte.digital;

import com.deloitte.digital.Exceptions.InvalidActivityFormatException;
import com.deloitte.digital.Exceptions.InvalidScheduleException;
import com.deloitte.digital.Model.Activity;
import com.deloitte.digital.Model.DigitalDaySchedule;
import com.deloitte.digital.Service.DigitalDayScheduler;
import com.deloitte.digital.Service.ScheduleIntialiser;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DigitalAwayDay {

    private static final Logger logger = Logger.getLogger(DigitalAwayDay.class.getName());

    private DigitalDayScheduler digitalDayScheduler;

    /**
     * Method to call the Digital Day Scheduler
     * @param filename input file name
     * @param sprintName sprint name
     * @throws InvalidActivityFormatException if the lines are not formatted as required
     * @throws InvalidScheduleException if the time/duration do not add up to the required value
     */
    public void getDigitalDaySchedule(String filename, String sprintName) throws InvalidActivityFormatException, InvalidScheduleException{
            ScheduleIntialiser scheduleIntialiser = new ScheduleIntialiser();
            List<Activity> activities =
                    scheduleIntialiser.getActivitiesFromInput(filename, sprintName);
            if (!activities.isEmpty()) {
                digitalDayScheduler = new DigitalDayScheduler(new DigitalDaySchedule());
                digitalDayScheduler.initialiseDigitalDaySchedule(activities);
                digitalDayScheduler.formTeams();
                digitalDayScheduler.getListOfPossibleActivities();
            }
    }

    public DigitalDayScheduler getDigitalDayScheduler() {
        return digitalDayScheduler;
    }

    public static void main(String[] args) {

        try {
            DigitalAwayDay digitalAwayDay = new DigitalAwayDay();
            digitalAwayDay.getDigitalDaySchedule("activities.txt", "sprint");
        } catch(InvalidActivityFormatException|InvalidScheduleException|Exception e) {
            logger.log(Level.SEVERE, "Error in processing the file.");
        }
    }
}
