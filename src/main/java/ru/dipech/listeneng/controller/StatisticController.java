package ru.dipech.listeneng.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dipech.listeneng.entity.api.ApiResponse;
import ru.dipech.listeneng.entity.persistence.StatisticValue;
import ru.dipech.listeneng.service.StatisticService;
import ru.dipech.listeneng.service.UserService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final UserService userService;
    private final StatisticService statisticService;

    @PostMapping("/add")
    public ApiResponse<Object> add(@AuthenticationPrincipal Principal principal,
                                   @RequestBody RequestData data) {
        var user = userService.getByEmail(principal.getName());
        statisticService.add(user, data.getEntryId(), data.getStatisticValue());
        return ApiResponse.ok();
    }

    @Getter
    @Setter
    public static class RequestData {
        private UUID entryId;
        private StatisticValue statisticValue;
    }

}
