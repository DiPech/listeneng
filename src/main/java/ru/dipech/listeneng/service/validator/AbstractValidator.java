package ru.dipech.listeneng.service.validator;

import org.apache.commons.lang3.StringUtils;
import ru.dipech.listeneng.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractValidator<T> implements Validator<T> {

    public static final String DELIMITER = "\n";

    @Override
    public void validate(T t) throws ValidationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (violations.size() > 0) {
            throw new ValidationException(composeMessage(violations));
        }
        doValidate(t);
    }

    @Override
    public void validate(Collection<T> list) throws ValidationException {
        List<String> messages = new ArrayList<>();
        for (T t : list) {
            try {
                validate(t);
            } catch (ValidationException exception) {
                messages.add(exception.getMessage());
            }
        }
        if (messages.size() > 0) {
            throw new ValidationException(String.join(DELIMITER, messages));
        }
    }

    private String composeMessage(Set<ConstraintViolation<T>> violations) {
        return violations.stream()
            .map(violation -> String.format("%s %s%s.",
                violation.getPropertyPath(),
                violation.getMessage(),
                getInvalidValueMessage(violation.getInvalidValue())))
            .map(StringUtils::capitalize)
            .collect(Collectors.joining(DELIMITER));
    }

    private String getInvalidValueMessage(Object invalidValue) {
        if (invalidValue == null) {
            return "";
        }
        String value = invalidValue.toString();
        return value.length() > 0 ? " (given '" + value + "')" : "";

    }

    protected abstract void doValidate(T t);

}
