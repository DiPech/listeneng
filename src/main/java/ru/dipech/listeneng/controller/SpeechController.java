package ru.dipech.listeneng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dipech.listeneng.entity.api.ApiResponse;
import ru.dipech.listeneng.entity.persistence.EntryPart;
import ru.dipech.listeneng.service.EntrySpeechService;
import ru.dipech.listeneng.service.TextSpeechService;
import ru.dipech.listeneng.service.UserService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/speech")
@RequiredArgsConstructor
public class SpeechController {

    private final UserService userService;
    private final TextSpeechService textSpeechService;
    private final EntrySpeechService entrySpeechService;

    @PostMapping("/text/{text}")
    public ApiResponse<String> text(@PathVariable String text) {
        var filePath = textSpeechService.speech(text);
        return ApiResponse.ok(filePath.getFileName().toString());
    }

    @PostMapping("/entry/{id}/{part}")
    public ApiResponse<String> entry(@AuthenticationPrincipal Principal principal,
                                     @PathVariable UUID id, @PathVariable EntryPart part) {
        var user = userService.getByEmail(principal.getName());
        var filePath = entrySpeechService.speech(user, id, part);
        return ApiResponse.ok(filePath.getFileName().toString());
    }

}
