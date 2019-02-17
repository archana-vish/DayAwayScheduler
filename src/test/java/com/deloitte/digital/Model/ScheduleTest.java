package com.deloitte.digital.Model;

import com.deloitte.digital.Service.DigitalDayScheduler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    Schedule schedule = new DigitalDayScheduler(new DigitalDaySchedule());

    @Test
    void getPreLunchMinutes() {
        assertEquals(180, schedule.getPreLunchDuration());
    }

    @Test
    void getPostLunchMinutes() {
        assertEquals(240, schedule.getPostLunchMaxDuration());
    }
}