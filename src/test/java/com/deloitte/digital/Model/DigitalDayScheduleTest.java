package com.deloitte.digital.Model;

import com.deloitte.digital.Exceptions.InsufficientActivitiesException;
import com.deloitte.digital.Exceptions.InvalidActivityFormatException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DigitalDayScheduleTest {

    @Test
    public void should_throw_exception_if_insufficient_activities_in_list() {
        DigitalDaySchedule digitalDaySchedule = new DigitalDaySchedule();

        digitalDaySchedule.setDigitalDayActivities(Arrays.asList(
                new Activity("Duck Herding", 60l)
        ));



        Throwable exception = assertThrows(InsufficientActivitiesException.class, () -> {
            digitalDaySchedule.setTeamSize();
        });
        assertEquals(exception.getMessage(),"Insufficient activities to form teams");
    }

}