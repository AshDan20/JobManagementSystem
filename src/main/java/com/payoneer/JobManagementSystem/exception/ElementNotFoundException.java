package com.payoneer.JobManagementSystem.exception;

public class ElementNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4262521951416399275L;

    public ElementNotFoundException(String message) {
        super(message);
    }
}
