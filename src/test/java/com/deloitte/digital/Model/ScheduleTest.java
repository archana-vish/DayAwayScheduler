package com.deloitte.digital.Model;

import com.deloitte.digital.Service.DigitalDayScheduler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


class ScheduleTest {

    Schedule schedule = new DigitalDaySchedule();

    @Test
    void getPreLunchMinutes() {
        assertEquals(180, schedule.getPreLunchDuration());
    }

    @Test
    void getPostLunchMinutes() {
        assertEquals(240, schedule.getPostLunchMaxDuration());
    }
}