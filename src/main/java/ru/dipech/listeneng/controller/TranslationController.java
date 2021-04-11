package ru.dipech.listeneng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dipech.listeneng.entity.api.ApiResponse;
import ru.dipech.listeneng.entity.persistence.EntryPart;
import ru.dipech.listeneng.service.EntryTranslationService;
import ru.dipech.listeneng.service.TextTranslationService;
import ru.dipech.listeneng.service.UserService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/translate")
@RequiredArgsConstructor
public class TranslationController {

    private final UserService userService;
    private final TextTranslationService textTranslationService;
    private final EntryTranslationService entryTranslationService;

    @PostMapping("/text/{text}")
    public ApiResponse<String> text(@PathVariable String text) {
        String result = textTranslationService.translate(text);
        return ApiResponse.ok(result);
    }

    @PostMapping("/entry/{id}/{part}")
    public ApiResponse<String> entry(@AuthenticationPrincipal Principal principal,
                                     @PathVariable UUID id, @PathVariable EntryPart part) {
        var user = userService.getByEmail(principal.getName());
        return ApiResponse.ok(entryTranslationService.translate(user, id, part));
    }

}
