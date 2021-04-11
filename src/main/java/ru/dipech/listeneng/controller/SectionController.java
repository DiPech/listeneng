package ru.dipech.listeneng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.dipech.listeneng.entity.api.ApiResponse;
import ru.dipech.listeneng.entity.dto.SectionListDto;
import ru.dipech.listeneng.entity.dto.SectionTreeDto;
import ru.dipech.listeneng.entity.persistence.Section;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.service.SectionDtoService;
import ru.dipech.listeneng.service.SectionService;
import ru.dipech.listeneng.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/section")
@RequiredArgsConstructor
public class SectionController {

    private final UserService userService;
    private final SectionService sectionService;
    private final SectionDtoService sectionDtoService;

    @GetMapping("/tree")
    public ApiResponse<List<SectionTreeDto>> tree(@AuthenticationPrincipal Principal principal) {
        User user = userService.getByEmail(principal.getName());
        List<Section> sections = sectionService.getRoot(user);
        sectionDtoService.init(user);
        return ApiResponse.ok(sectionDtoService.convertToTreeDto(sections));
    }

    @PostMapping("/list")
    public ApiResponse<List<SectionListDto>> list(@AuthenticationPrincipal Principal principal,
                                                  @RequestBody List<UUID> ids) {
        User user = userService.getByEmail(principal.getName());
        List<Section> sections = sectionService.getByIds(user, ids);
        sectionDtoService.init(user);
        return ApiResponse.ok(sectionDtoService.convertToListDto(sections));
    }

    @PostMapping("/update-tree")
    public ApiResponse<Section> updateTree(@AuthenticationPrincipal Principal principal,
                                           @RequestBody List<SectionTreeDto> sectionDtos) {
        User user = userService.getByEmail(principal.getName());
        sectionService.syncSections(user, sectionDtos);
        return ApiResponse.ok();
    }

    @PostMapping("/update-list")
    public ApiResponse<Section> updateList(@AuthenticationPrincipal Principal principal,
                                           @RequestBody List<SectionListDto> sectionDtos) {
        User user = userService.getByEmail(principal.getName());
        sectionService.syncEntries(user, sectionDtos);
        return ApiResponse.ok();
    }

    @PostMapping("/delete")
    public ApiResponse<Section> delete(@AuthenticationPrincipal Principal principal,
                                       @RequestBody List<UUID> ids) {
        User user = userService.getByEmail(principal.getName());
        sectionService.delete(user, ids);
        return ApiResponse.ok();
    }

}
