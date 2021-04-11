package ru.dipech.listeneng.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.exception.ValidationException;
import ru.dipech.listeneng.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class NewUserValidator extends AbstractValidator<User> {

    private final UserRepository userRepository;

    @Override
    protected void doValidate(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ValidationException("User with given email already exists");
        }

        // TMP
        throw new ValidationException("New user registration is closed for now :(");
        // TMP
    }

}
