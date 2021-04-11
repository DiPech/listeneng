package ru.dipech.listeneng.exception;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super("User");
    }

}
