package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.dto.NewUserDto;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.entity.persistence.UserRole;
import ru.dipech.listeneng.exception.UserNotFoundException;
import ru.dipech.listeneng.repository.UserRepository;
import ru.dipech.listeneng.service.validator.NewUserValidator;
import ru.dipech.listeneng.service.validator.Validator;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final Validator<NewUserDto> userDtoValidator;
    private final NewUserValidator userValidator;
    private final FetchService fetchService;

    // Suppress to break circular dependencies exception
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        fetchService.fetch(user, "roles");
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        fetchService.fetch(user, "roles");
        return user;
    }

    @Override
    @Transactional
    public void addUser(NewUserDto userDto) {
        userDtoValidator.validate(userDto);
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(userRoleService.getUserRoles(UserRole.ROLE_USER));
        userValidator.validate(user);
        userRepository.save(user);
    }

}
