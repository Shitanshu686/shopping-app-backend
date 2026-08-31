package com.shitanshu.shopping.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shitanshu.shopping.storage.ImageStorageService;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageStorageService imageStorageService;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("image") MultipartFile image)
            throws IOException {

        String imageUrl =
                imageStorageService.storeImage(image);

        return ResponseEntity.ok(imageUrl);
    }
}