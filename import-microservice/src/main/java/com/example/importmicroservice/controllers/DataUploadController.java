package com.example.importmicroservice.controllers;

import com.example.importmicroservice.pojos.Message;
import com.example.importmicroservice.producers.Producer;
import com.example.importmicroservice.services.WasabiUploader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DataUploadController {

    private WasabiUploader wasabiUploader;
    private Producer producer;

    public DataUploadController(WasabiUploader wasabiUploader, Producer producer) {
        this.wasabiUploader = wasabiUploader;
        this.producer = producer;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file ) {

        String fileName = file.getOriginalFilename();
        try {
            wasabiUploader.uploadFile(fileName, file.getBytes());
            producer.sendUpdate(new Message(fileName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok("New Data Uploaded");
    }
}
