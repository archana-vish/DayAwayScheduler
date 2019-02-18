package com.deloitte.digital.Service;

import com.deloitte.digital.Exceptions.InsufficientActivitiesException;
import com.deloitte.digital.Exceptions.InvalidScheduleException;
import com.deloitte.digital.Model.*;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void initialiseDigitalDaySchedule(List<Activity> activities) throws InvalidScheduleException, InsufficientActivitiesException{
        digitalDaySchedule.setDigitalDayActivities(activities);
        digitalDaySchedule.setTotalActivitiesDuration();
        digitalDaySchedule.setTeamSize();
    }

    public void formTeams() throws  InvalidScheduleException {

        long totTeamsFormed = 0;
        LocalTime activityStartTime = DigitalDaySchedule.START_OF_DAY;

        while (totTeamsFormed < digitalDaySchedule.getTeamSize()) {


            List<Activity> allActivities = digitalDaySchedule.getDigitalDayActivities();
            activityStartTime = DigitalDaySchedule.START_OF_DAY;

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

            System.out.println("Starting at :: " + allActivities.get(counter));


            cachedActivities.add(allActivities.get(counter));

            ArrayList<Activity> eligibleForPreLunch = new ArrayList<>(allActivities);

            activityList.add(allActivities.get(counter));
            eligibleForPreLunch.removeAll(activityList);
            activityList = formActivityGroups(activityStartTime, eligibleForPreLunch, allActivities.get(counter).getDuration(), digitalDaySchedule.getPreLunchDuration(), digitalDaySchedule.getPreLunchDuration(), false);
            List<Activity> preLunchList = new ArrayList<>(activityList);
            logger.info("Prelunch group :: " + preLunchList);

            List<Activity> eligibleForPostLunch = digitalDaySchedule.getDigitalDayActivities();
            eligibleForPostLunch.removeAll(preLunchList);
            activityList.clear();

            cachedActivities.add(eligibleForPostLunch.get(0));
            activityList.add(eligibleForPostLunch.get(0));
            activityList = formActivityGroups(activityStartTime, eligibleForPostLunch.subList(1, eligibleForPostLunch.size()), eligibleForPostLunch.get(0).getDuration(), digitalDaySchedule.getPostLunchMaxDuration(), digitalDaySchedule.getPostLunchMinDuration(), true);

            List<Activity> postLunchList = new ArrayList<>(activityList);
            logger.info("Postlunch final group :: " + postLunchList);
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


    public List<Activity> formActivityGroups(LocalTime activityStartTime, List<Activity> otherActivities, long totalTime, long targetTime, long targetMinTime, boolean isMinTimeTaken) throws InvalidScheduleException{

        if (otherActivities.isEmpty()) {
            logger.log(Level.SEVERE, "Have run out of combinations. Please check activity timings");
            throw new InvalidScheduleException("Please check activity timings. They do not fit into the "+ (isMinTimeTaken?"Post-Lunch":"Pre-Lunch") + " schedule");
        }

        totalTime +=  otherActivities.get(0).getDuration();

        if (totalTime == targetTime){
            cachedActivities.add(otherActivities.get(0));
            activityList.add(otherActivities.get(0));
            activityStartTime.plusMinutes(activityList.get(0).getDuration());
        } else if (isMinTimeTaken && totalTime >= targetMinTime && totalTime < targetTime) {
            cachedActivities.add(otherActivities.get(0));
            activityList.add(otherActivities.get(0));
            activityStartTime.plusMinutes(activityList.get(0).getDuration());
        } else if (totalTime < targetTime ) {
            cachedActivities.add(otherActivities.get(0));
            activityList.add(otherActivities.get(0));
            activityStartTime = activityStartTime.plusMinutes(activityList.get(0).getDuration());
            formActivityGroups(activityStartTime, otherActivities.subList(1,otherActivities.size()), totalTime, targetTime, targetMinTime, isMinTimeTaken);
        } else  {
            // totalTime > targetTime
            totalTime -=  otherActivities.get(0).getDuration();
            formActivityGroups(activityStartTime, otherActivities.subList(1,otherActivities.size()), totalTime, targetTime, targetMinTime, isMinTimeTaken);
        }
        return activityList;
    }

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
            System.out.println("Printing Schedule for Team " + team);
            System.out.println("==================================");
            listOfTimetables.get(team - 1).stream()
                    .forEach(System.out::println);
        }

        return listOfTimetables;
    }

    public List<List<Timetable>> getListOfTimetables() {
        return listOfTimetables;
    }
}
