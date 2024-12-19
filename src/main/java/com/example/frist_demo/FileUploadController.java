package com.example.frist_demo;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
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
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.qrcode.*;
import com.google.zxing.client.j2se.*;
import com.google.zxing.*;
import com.google.zxing.common.*;
import com.google.zxing.Result.*;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


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
                    String [] names=name.split("/");
                    name =names[names.length-1];
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
                    System.out.println(parse_QSR(out_file));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Could not store files";
        }
        return "Files uploaded successfully";
    }
    private String parse_QSR(File file){
        try {
            // 读取二维码图片文件
            BufferedImage bufferedImage = ImageIO.read(file);
            // 创建LuminanceSource对象，用于读取二维码图片的像素数据
            LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
            // 创建BinaryBitmap对象，用于将像素数据转换为二值化图像
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
            // 创建MultiFormatReader对象，用于解析二维码
            MultiFormatReader multiFormatReader = new MultiFormatReader();
            // 解析二维码，获取解析结果
            Result result = multiFormatReader.decode(binaryBitmap);
            // 输出解析结果的信息，如文本内容、格式等
            return result.getText();
        } catch (NotFoundException | IOException e) {
            e.printStackTrace();
        }
        return "Failed to parse QSR";
    }
}