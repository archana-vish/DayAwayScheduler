package com.deloitte.digital.Service;

import com.deloitte.digital.Exceptions.InvalidActivityFormatException;
import com.deloitte.digital.Model.Activity;
import com.deloitte.digital.Model.Schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

/**
 * Class to load the activities from the file
 */
public class ScheduleIntialiser {

    String SPRINT = Schedule.SPRINT;
    static Set<String> activityTitles;
    static Logger logger = Logger.getLogger(ScheduleIntialiser.class.getName());


    // Read file to get list of activities

    /**
     * Method to read the input file and get the activities and duration
     * @param filename the input file to read activities
     * @param sprintname denotes variable to identify Sprint eg: 'sprint'
     * @return List of activities
     * @throws InvalidActivityFormatException
     */
    public List<Activity> getActivitiesFromInput(String filename, String sprintname) throws InvalidActivityFormatException {

        List<Activity> activityList = new ArrayList<>();
        activityTitles = new HashSet<>();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + filename)
        ))) {
            activityList = in.lines()
                    .distinct() // remove duplicates if any
                    .map(line -> line.replaceAll(sprintname, SPRINT))
                    .map(ScheduleIntialiser::addActivity)
                    .filter(activity -> !activity.getTitle().isEmpty()) // remove empty activity
                    .sorted(Comparator.comparing(Activity::getDuration).reversed())
                    .collect(toList());

            if (activityList.size() == 0) {
                throw new InvalidActivityFormatException("Empty activity list.");
            }

            if (activityList.size() > activityTitles.size()) {
                throw new InvalidActivityFormatException("List contains duplicate. Please check file.");
            }


        } catch (IOException  e) {
            logger.log(Level.SEVERE,"Error in reading activities file. Ensure the filename is correct.");
        }
        return activityList;
    }


    /**
     * Method to create an Activity
     * @param line eg: Duck Herding 60min
     * @return {'Duck Herding' , 60l}
     */

    public static Activity addActivity(String line) {
        String activity = "";
        long duration = 0;

        try {
            if (line.isEmpty()) {
                throw new InvalidActivityFormatException("Empty activity");
            } else if (!line.matches("^.+[0-9]+\\dmin")) {
                throw new InvalidActivityFormatException("Invalid line :: " + line);
            } else {
                activity = line.substring(0, line.lastIndexOf(" "));
                duration = Long.parseLong(line.substring(line.lastIndexOf(" ")+1, line.lastIndexOf("min")));
                if (duration <= 0) {
                    throw new InvalidActivityFormatException("Activity time cannot be less than 1 minute");
                }
                if (activityTitles.contains(activity.trim().toUpperCase())) {
                    throw new InvalidActivityFormatException("List contains duplicate activity :: " + activity);
                } else {
                    activityTitles.add(activity.trim().toUpperCase());
                }
            }
        } catch (InvalidActivityFormatException invalidActivityFormatException) {
            logger.log(Level.SEVERE,"Error in handling activity " + invalidActivityFormatException.getMessage());
        }
            return new Activity(activity, duration);
    }
}
