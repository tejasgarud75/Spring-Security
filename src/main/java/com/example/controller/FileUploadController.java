package com.example.controller;

import com.example.errors.BadRequestAlertException;
import com.example.service.FileUploadService;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload-file")
public class FileUploadController {

    private final FileUploadService service;

    public FileUploadController(FileUploadService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public ResponseEntity<String> getRequestById(@PathVariable Long id) throws IOException {
        Optional<String> request = service.getFile(id);
        return request.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestPart("image") MultipartFile image)
            throws IOException, BadRequestAlertException {
        return ResponseEntity.ok(service.uploadFile(image));
    }

}
