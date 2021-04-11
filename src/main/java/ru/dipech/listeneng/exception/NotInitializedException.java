package ru.dipech.listeneng.exception;

public class NotInitializedException extends ApplicationException {

    public NotInitializedException() {
        super("Initialize the service first");
    }

}
