package com.deloitte.digital.Service;

import com.deloitte.digital.Exceptions.InvalidScheduleException;
import com.deloitte.digital.Model.Activity;
import com.deloitte.digital.Model.DigitalDaySchedule;
import com.deloitte.digital.Model.Schedule;
import com.deloitte.digital.Model.Timetable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to start scheduling activities in teams
 *
 */
public class DigitalDayScheduler{

    DigitalDaySchedule digitalDaySchedule;

    List<List<Activity>> listOfPossibleActivities = new ArrayList<>();
    List<List<Timetable>> listOfTimetables = new ArrayList<>();

    List<Activity> cachedActivities = new ArrayList<>();
    List<Activity> activityList = new ArrayList();

    static Logger logger = Logger.getLogger(DigitalDaySchedule.class.getName());

    public DigitalDayScheduler(DigitalDaySchedule digitalDaySchedule) {
        this.digitalDaySchedule = digitalDaySchedule;
    }

    /**
     * Method to initialise the digital day schedule
     *  1. Set Up the digital activity list from the input file
     *  2. Find the total activity duration from the list
     *  3. Find the team size for the given set of activities
     * @param activities list of activities read from the file
     * @throws InvalidScheduleException if there are not enough activities to fill the day's schedule
     */
    public void initialiseDigitalDaySchedule(List<Activity> activities) throws InvalidScheduleException{
        digitalDaySchedule.setDigitalDayActivities(activities);
        digitalDaySchedule.setTotalActivitiesDuration();
        digitalDaySchedule.setTeamSize();
    }

    /**
     * Method to form teams
     * Get items from the list and form add them to either Pre-lunch or Post-lunch bucket
     * Call a function recursively till the total duration of activities add up to the required target time
     * Add selected activities to a cache to eliminate them when the next team is formed
     * @throws InvalidScheduleException
     */
    public void formTeams() throws  InvalidScheduleException {

        long totTeamsFormed = 0;
        //LocalTime activityStartTime = DigitalDaySchedule.START_OF_DAY;

        while (totTeamsFormed < digitalDaySchedule.getTeamSize()) {

            List<Activity> allActivities = digitalDaySchedule.getDigitalDayActivities();

            if (!cachedActivities.isEmpty()) {
                allActivities.removeAll(cachedActivities);
            }

            int counter = 0;
            // Start with item whose duration is less than or equal to the target time
            ListIterator<Activity> listIterator = allActivities.listIterator();
            while(listIterator.hasNext()) {
                if (listIterator.next().getDuration() <= digitalDaySchedule.getPreLunchDuration()) {
                    break;
                } else {
                    counter++;
                }
            }

            cachedActivities.add(allActivities.get(counter));

            ArrayList<Activity> eligibleForPreLunch = new ArrayList<>(allActivities);

            activityList.add(allActivities.get(counter));
            eligibleForPreLunch.removeAll(activityList);
            activityList = formActivityGroups(eligibleForPreLunch, allActivities.get(counter).getDuration(), digitalDaySchedule.getPreLunchDuration(), digitalDaySchedule.getPreLunchDuration(), false);
            List<Activity> preLunchList = new ArrayList<>(activityList);

            List<Activity> eligibleForPostLunch = digitalDaySchedule.getDigitalDayActivities();
            eligibleForPostLunch.removeAll(preLunchList);
            activityList.clear();

            cachedActivities.add(eligibleForPostLunch.get(0));
            activityList.add(eligibleForPostLunch.get(0));
            activityList = formActivityGroups(eligibleForPostLunch.subList(1, eligibleForPostLunch.size()), eligibleForPostLunch.get(0).getDuration(), digitalDaySchedule.getPostLunchMaxDuration(), digitalDaySchedule.getPostLunchMinDuration(), true);

            List<Activity> postLunchList = new ArrayList<>(activityList);
            activityList.clear();

            List<Activity> finalList = new ArrayList<>();
            finalList.addAll(preLunchList);
            finalList.add(new Activity("Lunch Break", 60));
            finalList.addAll(postLunchList);
            finalList.add(new Activity(DigitalDaySchedule.MANDATORY_END_ACTIVITY, DigitalDaySchedule.MANDATORY_END_ACTIVITY_DURATION));

            listOfPossibleActivities.add(new ArrayList<>(finalList));
            finalList.clear();
            totTeamsFormed++;
        }
    }


    /**
     * Recursive function to add activities to the pre/post lunch buckets
     * @param otherActivities is the sublist of activities other than the one already cached
     * @param totalTime total time the selected activities have added up to
     * @param targetTime time needed for the pre lunch or post lunch (max) session
     * @param targetMinTime time needed for the post lunch (min) session
     * @param isMinTimeTaken to check if minimum time has to be taken into account
     * @return List of selected activities
     * @throws InvalidScheduleException
     */
    public List<Activity> formActivityGroups(List<Activity> otherActivities, long totalTime, long targetTime, long targetMinTime, boolean isMinTimeTaken) throws InvalidScheduleException{

        if (otherActivities.isEmpty()) {
            logger.log(Level.SEVERE, "Have run out of combinations. Please check activity timings");
            throw new InvalidScheduleException("Please check activity timings. They do not fit into the "+ (isMinTimeTaken?"Post-Lunch":"Pre-Lunch") + " schedule");
        }

        totalTime +=  otherActivities.get(0).getDuration();

        if (totalTime == targetTime){
            cachedActivities.add(otherActivities.get(0));
            activityList.add(otherActivities.get(0));
        } else if (isMinTimeTaken && totalTime >= targetMinTime && totalTime < targetTime) {
            cachedActivities.add(otherActivities.get(0));
            activityList.add(otherActivities.get(0));
        } else if (totalTime < targetTime ) {
            cachedActivities.add(otherActivities.get(0));
            activityList.add(otherActivities.get(0));
            formActivityGroups(otherActivities.subList(1,otherActivities.size()), totalTime, targetTime, targetMinTime, isMinTimeTaken);
        } else  {
            // totalTime > targetTime
            totalTime -=  otherActivities.get(0).getDuration();
            formActivityGroups(otherActivities.subList(1,otherActivities.size()), totalTime, targetTime, targetMinTime, isMinTimeTaken);
        }
        return activityList;
    }

    /**
     * Method to form the timetable and print the timetables
     * @return
     */
    public List<List<Timetable>> getListOfPossibleActivities() {
        List<Timetable> timetables = new ArrayList<>();
        ListIterator<List<Activity>> listListIterator = listOfPossibleActivities.listIterator();
        LocalTime activityStartTime = Schedule.START_OF_DAY;
        while(listListIterator.hasNext()) {
            List<Activity> activityList = listListIterator.next();
            timetables.add(new Timetable(activityList.get(0).getTitle(), activityStartTime, activityList.get(0).getDuration()));
            for(int index = 1; index  < activityList.size(); index++) {
                activityStartTime = activityStartTime.plusMinutes(activityList.get(index - 1).getDuration());
                timetables.add(new Timetable(activityList.get(index).getTitle(), activityStartTime, activityList.get(index).getDuration()));
            }
            listOfTimetables.add(new ArrayList<>(timetables));
            timetables.clear();
            activityStartTime = Schedule.START_OF_DAY;
        }

        for (int team = 1; team <= digitalDaySchedule.getTeamSize(); team++) {
            System.out.println("==================================");
            System.out.println("Printing Schedule for Team " + team);
            System.out.println("==================================");
            listOfTimetables.get(team - 1).stream()
                    .forEach(System.out::println);
        }

        return listOfTimetables;
    }

    /**
     * Method to get the list of timetables. Mainly used for testing
     * @return List of list of timetables
     */
    public List<List<Timetable>> getListOfTimetables() {
        return listOfTimetables;
    }
}
