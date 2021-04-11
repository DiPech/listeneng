package ru.dipech.listeneng.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.entity.persistence.UserRole;
import ru.dipech.listeneng.exception.UserNotFoundException;
import ru.dipech.listeneng.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user;
        try {
            user = userService.getByEmail(email);
        } catch (UserNotFoundException exception) {
            throw new UsernameNotFoundException(exception.getMessage(), exception);
        }
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(UserRole::getName)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        return new UserDetailsImpl(user.getEmail(), user.getPassword(), authorities);
    }

}
