package ru.dipech.listeneng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dipech.listeneng.entity.api.ApiResponse;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.exception.ApplicationException;
import ru.dipech.listeneng.service.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/tech")
@RequiredArgsConstructor
public class TechController {

    private final DevelopmentService developmentService;
    private final FileCacheService fileCacheService;
    private final UserService userService;
    private final SectionService sectionService;
    private final InitialDataService initialDataService;

    @GetMapping({"/populate", "/populate/{force}"})
    public ApiResponse<User> populate(@AuthenticationPrincipal Principal principal,
                                      @PathVariable(required = false) Boolean force) {
        ensureIsDev();
        User user = userService.getByEmail(principal.getName());
        initialDataService.populate(force != null ? force : false);
        sectionService.updateGlobalPriorities(user);
        return ApiResponse.ok();
    }

    @GetMapping({"/flush-cache"})
    public ApiResponse<Object> flushCache() {
        fileCacheService.deleteAll();
        return ApiResponse.ok();
    }

    private void ensureIsDev() {
        if (!developmentService.isDev()) {
            throw new ApplicationException("Cannot use this endpoint in PROD env");
        }
    }

}
