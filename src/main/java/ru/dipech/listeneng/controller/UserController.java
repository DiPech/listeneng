package ru.dipech.listeneng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dipech.listeneng.entity.api.ApiResponse;
import ru.dipech.listeneng.entity.persistence.Permission;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.service.UserService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/current")
    public ApiResponse<User> current(@AuthenticationPrincipal Principal principal) {
        User user = userService.getByEmail(principal.getName());
        return ApiResponse.ok(user);
    }

    @Secured(Permission.ADMIN)
    @GetMapping("/get/{id}")
    public ApiResponse<User> get(@PathVariable UUID id) {
        User user = userService.getById(id);
        return ApiResponse.ok(user);
    }

}
