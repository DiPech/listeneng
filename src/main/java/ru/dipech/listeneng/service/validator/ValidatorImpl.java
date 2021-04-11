package ru.dipech.listeneng.service.validator;

import org.springframework.stereotype.Service;

@Service
public class ValidatorImpl<T> extends AbstractValidator<T> {

    @Override
    protected void doValidate(T t) {

    }

}
