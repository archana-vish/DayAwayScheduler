package com.deloitte.digital.Exceptions;

/**
 * Exception class to handle invalid activity format
 */
public class InvalidActivityFormatException extends Throwable {

    public InvalidActivityFormatException(String message) {
        super(message);
    }
}
