package ru.dipech.listeneng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dipech.listeneng.entity.api.ApiResponse;
import ru.dipech.listeneng.entity.dto.NewUserDto;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.service.FetchService;
import ru.dipech.listeneng.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.dipech.listeneng.util.ThrowingRunnable.throwingRunnable;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {

    private final UserService userService;
    private final FetchService fetchService;
    private final PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

    @PostMapping("/login")
    public ApiResponse<User> login(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody NewUserDto userDto) {
        throwingRunnable(() -> request.login(userDto.getEmail(), userDto.getPassword()));
        var auth = SecurityContextHolder.getContext().getAuthentication();
        persistentTokenBasedRememberMeServices.loginSuccess(request, response, auth);
        User user = userService.getByEmail(userDto.getEmail());
        fetchService.fetch(user, "roles");
        return ApiResponse.ok(user);
    }

    @PostMapping("/register")
    public ApiResponse<User> register(HttpServletRequest request, HttpServletResponse response,
                                      @RequestBody NewUserDto userDto) {
        userService.addUser(userDto);
        return login(request, response, userDto);
    }

}
