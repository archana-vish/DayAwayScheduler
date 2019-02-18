package com.deloitte.digital;

import com.deloitte.digital.Exceptions.InvalidActivityFormatException;
import com.deloitte.digital.Exceptions.InvalidScheduleException;
import com.deloitte.digital.Model.DigitalDaySchedule;
import com.deloitte.digital.Service.ScheduleIntialiser;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class DigitalAwayDayTest {



    DigitalAwayDay digitalAwayDay;
    ScheduleIntialiser scheduleIntialiser;
    DigitalDaySchedule digitalDaySchedule;

    //TESTS
    // 1. File does not exist
    @Test
    void given_wrong_input_file_must_throw_exception() {
        scheduleIntialiser = new ScheduleIntialiser();
        assertThrows(NullPointerException.class,() -> {
            scheduleIntialiser.getActivitiesFromInput("wrongfilename.txt","sprint");
        });
    }

    // 2. Empty file
    @Test
    void given_an_empty_file_must_stop_processing() {
        scheduleIntialiser = new ScheduleIntialiser();
        Throwable exception = assertThrows(InvalidActivityFormatException.class, () -> {
            scheduleIntialiser.getActivitiesFromInput("emptyfile.txt", "sprint");
        });
        assertEquals(exception.getMessage(),"Empty activity list.");
    }

    // 3. Incorrect format in activity file
    @Test
    void given_an_incorrect_single_line_must_stop_processing() {
        scheduleIntialiser = new ScheduleIntialiser();
        Throwable exception = assertThrows(InvalidActivityFormatException.class, () -> {
            scheduleIntialiser.getActivitiesFromInput("invalidentry.txt", "sprint");
        });
        assertEquals(exception.getMessage(),"Empty activity list.");
    }

    // 4. Duplicate entries
    @Test
    void should_remove_duplicate_activities() {
        try {
            scheduleIntialiser = new ScheduleIntialiser();
            assertEquals(1, scheduleIntialiser.getActivitiesFromInput("duplicateEntries.txt", "sprint").size());
        } catch(InvalidActivityFormatException invalidActivityFormat) {}
    }

    // 4.2. Duplicate entries
    @Test
    void should_handle_duplicate_activities_with_different_duration() {
        scheduleIntialiser = new ScheduleIntialiser();

        Throwable exception = assertThrows(InvalidActivityFormatException.class, () -> {
            scheduleIntialiser.getActivitiesFromInput("duplicateActivity.txt", "sprint");
        });
        assertEquals(exception.getMessage(),"List contains duplicate. Please check file.");
    }

    // 5. Insufficient Activities to fill the day
    @Test
    void should_throw_exception_if_insufficient_activities_in_list() {
        digitalAwayDay = new DigitalAwayDay();

        Throwable exception = assertThrows(InvalidScheduleException.class, () -> {
            digitalAwayDay.getDigitalDaySchedule("insufficientActivities.txt", "sprint");
        });
        assertEquals(exception.getMessage(),"Activity timings not enough to fill the Digital Day Schedule");
    }

    // 6. Insufficient activities to form teams - above test ensures this condition

    // 7. Not enough to form strict morning schedule
    @Test
    void should_throw_exception_if_activities_do_not_fit_in_morn_slots() {
        digitalAwayDay = new DigitalAwayDay();

        Throwable exception = assertThrows(InvalidScheduleException.class, () -> {
            digitalAwayDay.getDigitalDaySchedule("invalidMornSchedule.txt", "sprint");
        });
        assertEquals(exception.getMessage(),"Please check activity timings. They do not fit into the Pre-Lunch schedule");

    }

    // 9. Not enough to form afternoon schedule
    @Test
    void should_throw_exception_if_activities_do_not_fit_in_afternoon_slots() {
        digitalAwayDay = new DigitalAwayDay();

        Throwable exception = assertThrows(InvalidScheduleException.class, () -> {
            digitalAwayDay.getDigitalDaySchedule("invalidNoonSchedule.txt", "sprint");
        });
        assertEquals(exception.getMessage(),"Please check activity timings. They do not fit into the Post-Lunch schedule");

    }



    // 10. Positive read
    @Test
    void should_form_two_sets_of_timetables_for_correct_known_file() {
        try {
            digitalAwayDay = new DigitalAwayDay();
            digitalAwayDay.getDigitalDaySchedule("activities.txt", "sprint");
            assertEquals(2, digitalAwayDay.getDigitalDayScheduler().getListOfTimetables().size());
        } catch(InvalidActivityFormatException|InvalidScheduleException|Exception e) {
            System.out.println(e.getMessage());
        }
    }





}