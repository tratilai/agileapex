package com.agileapex;

public class AgileApexException extends RuntimeException {
    private static final long serialVersionUID = -5671577830282387636L;

    public AgileApexException(String message) {
        super(message);
    }

    public AgileApexException(String message, Exception e) {
        super(message, e);
    }
}
