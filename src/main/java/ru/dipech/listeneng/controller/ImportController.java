package ru.dipech.listeneng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.dipech.listeneng.entity.api.ApiResponse;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.service.ImportService;
import ru.dipech.listeneng.service.UserService;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final UserService userService;
    private final ImportService importService;

    @PostMapping("/all")
    public ApiResponse<String> check(@AuthenticationPrincipal Principal principal,
                                     @RequestParam("file") MultipartFile file)
        throws IOException {
        User user = userService.getByEmail(principal.getName());
        importService.importAll(user, file.getInputStream());
        return ApiResponse.ok();
    }

}
