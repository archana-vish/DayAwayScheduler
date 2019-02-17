package com.deloitte.digital.Service;

import com.deloitte.digital.Exceptions.InvalidActivityFormatException;
import com.deloitte.digital.Model.Activity;
import com.deloitte.digital.Model.Schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

public class ScheduleIntialiser {

    String SPRINT = Schedule.SPRINT;
    static Logger logger = Logger.getLogger(ScheduleIntialiser.class.getName());

    // Read file to get list of activities
    public List<Activity> getActivitiesFromInput(String filename, String sprintname) {

        List<Activity> activityList = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + filename)
        ))) {
            activityList = in.lines()
                    .map(line -> line.replaceAll(sprintname, SPRINT))
                    .map(ScheduleIntialiser::addActivity)
                    .sorted(Comparator.comparing(Activity::getDuration).reversed())
                    .peek(System.out::println)
                    .collect(toList());
        } catch (IOException e) {
            logger.info("Error in reading activities file.");
        }
        return activityList;
    }


    public static Activity addActivity(String line) {
        String activity = "";
        long duration = 0;

        try {
            if (line.isEmpty()) {
                throw new InvalidActivityFormatException("Empty activity");
            } else if (!line.matches("^.+[0-9]+\\dmin")) {
                throw new InvalidActivityFormatException("Invalid line");
            } else {
                activity = line.substring(0, line.lastIndexOf(" "));
                duration = Long.parseLong(line.substring(line.lastIndexOf(" ")+1, line.lastIndexOf("min")));
                if (duration <= 0 || duration > 60) {
                    throw new InvalidActivityFormatException("Invalid time limit for activity");
                }
            }
        } catch (InvalidActivityFormatException invalidActivityFormatException) {
            logger.info("Error in handling activity " + invalidActivityFormatException.getMessage());
        }
        return new Activity(activity, duration);
    }

}
