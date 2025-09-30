package de.kiwious.toktik.controller;

import de.kiwious.toktik.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class FileController {
    private final FileUploadService fileUploadService;

    public FileController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        MultipartFile multipartFile = fileUploadService.upload(file);
        return ResponseEntity.ok("Upload erfolgreich: " + multipartFile.getOriginalFilename());
    }
}