package com.mediclinic.appointment_scheduler.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mediclinic.appointment_scheduler.domain.response.file.ResFileDTO;
import com.mediclinic.appointment_scheduler.service.FileService;
import com.mediclinic.appointment_scheduler.util.annotation.ApiMessage;
import com.mediclinic.appointment_scheduler.util.error.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final AuthController authController;

    private final SecurityFilterChain filterChain;

    @Value("${doan.upload-file.base-uri}")
    private String baseURI;

    private final FileService fileService;

    FileController(FileService fileService, SecurityFilterChain filterChain, AuthController authController) {
        this.fileService = fileService;
        this.filterChain = filterChain;
        this.authController = authController;
    }

    @PostMapping("/files")
    @ApiMessage("Upload Single File")
    public ResponseEntity<ResFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder)
            throws URISyntaxException, IOException, StorageException {
        // skip validate
        if (file.isEmpty() || file == null) {
            throw new StorageException("File is empty. Pls upload a file .");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid) {
            throw new StorageException("Invalid file extension. only allos " + allowedExtensions.toString());
        }

        // create a directory if not exist
        this.fileService.createUploadFolder(baseURI + folder);
        // store file
        String uploadFile = this.fileService.store(file, folder);
        ResFileDTO res = new ResFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok().body(res);
    }

}
