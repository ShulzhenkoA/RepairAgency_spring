package ua.javaexternal_shulzhenko.repair_agency.exceptions;

public class DataBaseInteractionException extends RuntimeException {

    public DataBaseInteractionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataBaseInteractionException(String message) { super(message); }
}