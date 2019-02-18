package com.deloitte.digital.Service;

import com.deloitte.digital.Exceptions.InvalidActivityFormatException;
import org.hamcrest.Factory;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ScheduleIntialiserTest {

    ScheduleIntialiser scheduleIntialiser;


    //TESTS

    // 1. Incorrect filename
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

    // 3. Incorrect lines
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

}