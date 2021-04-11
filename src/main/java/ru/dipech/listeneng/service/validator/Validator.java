package ru.dipech.listeneng.service.validator;

import ru.dipech.listeneng.exception.ValidationException;

import java.util.Collection;

public interface Validator<T> {

    void validate(T t) throws ValidationException;

    void validate(Collection<T> list) throws ValidationException;

}
