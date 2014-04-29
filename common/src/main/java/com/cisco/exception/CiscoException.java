package com.cisco.exception;

/**
 * User: Rost
 * Date: 23.04.2014
 * Time: 0:34
 */
public class CiscoException extends RuntimeException {


    public CiscoException(String message) {
        super(message);
    }

    public CiscoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CiscoException(Throwable cause) {
        super(cause);
    }
}
