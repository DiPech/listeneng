package ru.dipech.listeneng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dipech.listeneng.entity.FileScope;
import ru.dipech.listeneng.service.FileService;

import java.nio.file.Files;

import static ru.dipech.listeneng.util.ThrowingSupplier.throwingSupplier;

@RestController
@RequestMapping("/api/download")
@RequiredArgsConstructor
public class DownloadController {

    private final FileService fileService;

    @GetMapping("/file/{name:.+}")
    public ResponseEntity<Resource> check(@PathVariable("name") String fileName) {
        var path = fileService.get(fileName, FileScope.PUBLIC);
        var fileBytes = throwingSupplier(() -> Files.readAllBytes(path));
        var resource = new ByteArrayResource(fileBytes);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
            .contentLength(path.toFile().length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

}
