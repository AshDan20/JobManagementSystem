package com.payoneer.JobManagementSystem.exception;

/**
 * @author Ashish Dandekar
 *
 */
public class JobManagementException extends RuntimeException {

    private static final long serialVersionUID = 4654405612963188234L;

    public JobManagementException(String message){
        super(message);
    }
}
