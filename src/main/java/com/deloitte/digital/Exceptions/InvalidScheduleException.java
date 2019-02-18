package com.deloitte.digital.Exceptions;

/**
 * Exception class to handle invalid schedule exception
 */
public class InvalidScheduleException extends  Throwable {

    public InvalidScheduleException(String message) {
        super(message);
    }
}
