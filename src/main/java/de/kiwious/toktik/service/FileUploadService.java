package de.kiwious.toktik.service;

import de.kiwious.toktik.service.aws.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {
    private final S3Service s3Service;

    public FileUploadService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public MultipartFile upload(MultipartFile file) throws IOException, InterruptedException {
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);
        s3Service.upload(file.getOriginalFilename(), tempFile);

        String thumnailName = file.getOriginalFilename().replace(".mp4", ".jpg");
        File thumbnailFile = s3Service.generateThumbnail(tempFile, thumnailName);
        s3Service.uploadThumbnail(thumnailName, thumbnailFile);

        tempFile.delete();
        thumbnailFile.delete();
        return file;
    }
}
