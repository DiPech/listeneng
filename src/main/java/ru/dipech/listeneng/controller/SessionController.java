package ru.dipech.listeneng.controller;

import ru.dipech.listeneng.entity.api.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @GetMapping("/check")
    public ApiResponse<Object> check(@AuthenticationPrincipal Principal principal) {
        if (principal == null) {
            return ApiResponse.error("You aren't logged in");
        }
        return ApiResponse.ok();
    }

}
