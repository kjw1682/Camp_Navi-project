package com.demo.campingnavi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
public class FileManageController {

    //본인 프로젝트 절대경로 입력
    private static final String FILE_UPLOAD_PATH = "C:\\Users\\user\\IdeaProjects\\CampingNavi\\src\\main\\resources\\static\\upload\\";

    @PostMapping("/uploadSummernoteImageFile")
    public ResponseEntity<?> uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFileName).substring(originalFileName.lastIndexOf("."));
        String savedFileName = UUID.randomUUID() + extension;

        try {
            File file = new File(FILE_UPLOAD_PATH + savedFileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            String fileUrl = "/upload/" + savedFileName;
            return ResponseEntity.ok().body(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }
}
