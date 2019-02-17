package com.deloitte.digital;

import com.deloitte.digital.Model.Activity;
import com.deloitte.digital.Model.DigitalDaySchedule;
import com.deloitte.digital.Service.DigitalDayScheduler;
import com.deloitte.digital.Service.ScheduleIntialiser;

import java.util.List;

public class DigitalAwayDay {

    public static void main(String[] args) {
        ScheduleIntialiser scheduleIntialiser = new ScheduleIntialiser();
        List<Activity> activities =
                scheduleIntialiser.getActivitiesFromInput("activities.txt","sprint");
        DigitalDayScheduler digitalDayScheduler = new DigitalDayScheduler(new DigitalDaySchedule());
        digitalDayScheduler.initialiseDigitalDaySchedule(activities);
        digitalDayScheduler.formTeams();
        digitalDayScheduler.getListOfPossibleActivities();
    }
}
