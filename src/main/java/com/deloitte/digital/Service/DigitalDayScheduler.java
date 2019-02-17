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
import java.util.logging.Logger;

public class DigitalDayScheduler implements Schedule{

    DigitalDaySchedule digitalDaySchedule;
    List<List<Activity>> listOfPossibleActivities = new ArrayList<>();
    List<List<Timetable>> listOfTimetables = new ArrayList<>();
    List<Activity> cachedActivities = new ArrayList<>();
    List<Activity> activityList = new ArrayList();
    static Logger logger = Logger.getLogger(DigitalDaySchedule.class.getName());

    public DigitalDayScheduler(DigitalDaySchedule digitalDaySchedule) {
        this.digitalDaySchedule = digitalDaySchedule;
    }

    public void initialiseDigitalDaySchedule(List<Activity> activities) {

        try {
            digitalDaySchedule.setDigitalDayActivities(activities);
            digitalDaySchedule.setTotalActivitiesDuration();
            digitalDaySchedule.setTeamSize();
        } catch(InvalidScheduleException invalidScheduleException) {
            logger.info(invalidScheduleException.getMessage());
        }
    }

    public void formTeams() {

        long totTeamsFormed = 0;

        while (totTeamsFormed < digitalDaySchedule.getTeamSize()) {


            List<Activity> allActivities = digitalDaySchedule.getDigitalDayActivities();

            if (!cachedActivities.isEmpty()) {
                allActivities.removeAll(cachedActivities);
            }

            int counter = 0;
            cachedActivities.add(allActivities.get(counter));

            activityList.add(allActivities.get(0));
            activityList = formActivityGroups(allActivities.subList(1, allActivities.size()), allActivities.get(counter).getDuration(), getPreLunchDuration(), getPostLunchMinDuration(), false);
            List<Activity> preLunchList = new ArrayList<>(activityList);
            logger.info("Prelunch group :: " + preLunchList);

            List<Activity> eligibleForPostLunch = digitalDaySchedule.getDigitalDayActivities();
            eligibleForPostLunch.removeAll(preLunchList);
            activityList.clear();

            cachedActivities.add(eligibleForPostLunch.get(0));
            activityList.add(eligibleForPostLunch.get(0));
            activityList = formActivityGroups(eligibleForPostLunch.subList(1, eligibleForPostLunch.size()), eligibleForPostLunch.get(0).getDuration(), getPostLunchMaxDuration(), getPostLunchMinDuration(), true);

            List<Activity> postLunchList = new ArrayList<>(activityList);
            logger.info("Postlunch final group :: " + postLunchList);
            activityList.clear();

            List<Activity> finalList = new ArrayList<>();
            finalList.addAll(preLunchList);
            finalList.add(new Activity("Lunch Break", 60));
            finalList.addAll(postLunchList);
            finalList.add(new Activity(MANDATORY_END_ACTIVITY, MANDATORY_END_ACTIVITY_DURATION));

            listOfPossibleActivities.add(new ArrayList<>(finalList));
            finalList.clear();
            digitalDaySchedule.resetList();
            totTeamsFormed++;
        }
    }


    public List<Activity> formActivityGroups(List<Activity> otherActivities, long totalTime, long targetTime, long targetMinTime, boolean isMinTimeTaken) {


        totalTime +=  otherActivities.get(0).getDuration();

        if (totalTime == targetTime){
            cachedActivities.add(otherActivities.get(0));
            activityList.add(otherActivities.get(0));
        } else if (isMinTimeTaken && totalTime >= targetMinTime) {
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



    public List<List<Timetable>> getListOfPossibleActivities() {
        List<Timetable> timetables = new ArrayList<>();
        ListIterator<List<Activity>> listListIterator = listOfPossibleActivities.listIterator();
        LocalTime activityStartTime = Schedule.START_OF_DAY;
        while(listListIterator.hasNext()) {
            List<Activity> activityList = listListIterator.next();
            timetables.add(new Timetable(activityList.get(0).getTitle(), activityStartTime));
            for(int index = 1; index  < activityList.size(); index++) {
                activityStartTime = activityStartTime.plusMinutes(activityList.get(index).getDuration());
                timetables.add(new Timetable(activityList.get(index).getTitle(), activityStartTime));
            }
            listOfTimetables.add(new ArrayList<>(timetables));
            timetables.clear();
            activityStartTime = Schedule.START_OF_DAY;
        }

        listOfTimetables.stream()
                .flatMap(line -> line.stream())
                .forEach(System.out::println);
        return listOfTimetables;
    }
}
