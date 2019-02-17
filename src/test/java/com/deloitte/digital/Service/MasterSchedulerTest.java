package com.deloitte.digital.Service;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MasterSchedulerTest {



    @Test
    void addActivity() {
        String line = "Duck Herding 60min";
        if (line.matches("^[A-Za-z0-9\\s]+\\w[0-9]*\\dmin")) {
            System.out.println("Match");
        } else {
            System.out.println("No match");
        }

        ScheduleIntialiser scheduleIntialiser = new ScheduleIntialiser();
        ScheduleIntialiser.addActivity("Duck Herding 60min");



    }

    @Test
    void testDuration(){
        String line = "Duck racing 600min";
        System.out.println(line.substring(line.lastIndexOf(" ")+1, line.lastIndexOf("min")));
    }


}