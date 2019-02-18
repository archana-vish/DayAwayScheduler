package com.deloitte.digital.Exceptions;

/**
 *  Exception class to handle insufficient activities in the input file
 */

public class InsufficientActivitiesException extends Throwable{
    public InsufficientActivitiesException(String message) {
        super(message);
    }
}
