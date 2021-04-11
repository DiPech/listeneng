package ru.dipech.listeneng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dipech.listeneng.entity.api.ApiResponse;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.service.ExportService;
import ru.dipech.listeneng.service.UserService;

import java.nio.file.Path;
import java.security.Principal;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final UserService userService;
    private final ExportService exportService;

    @GetMapping("/all")
    public ApiResponse<String> check(@AuthenticationPrincipal Principal principal) {
        User user = userService.getByEmail(principal.getName());
        Path file = exportService.exportAll(user);
        return ApiResponse.ok(file.getFileName().toString());
    }

}
