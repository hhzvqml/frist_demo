package com.example.frist_demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @PostMapping("/uploadFolder")
    public String uploadFolder(@RequestParam("folder") MultipartFile[] files) {
        String folderName = "uploads_" + System.currentTimeMillis();
        Path rootLocation = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // 跳过空文件
                }


                if(file.getOriginalFilename().contains("jpg") || file.getOriginalFilename().contains("jpeg") || file.getOriginalFilename().contains("png")) {
                    String name=file.getOriginalFilename();
                    File out_file=new File("src/main/resources/static/uploads/"+name);
                    OutputStream outputStream = new FileOutputStream(out_file);
                    byte[] buffer=new byte[1024];
                    InputStream inputStream = file.getInputStream();
                    int length ;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.close();
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Could not store files";
        }
        return "Files uploaded successfully";
    }
}