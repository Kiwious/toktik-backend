package de.kiwious.toktik.service.aws;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String BUCKET_NAME = "bucket-toktik";

    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    public void upload(String key, File filePath) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key("videos/" + key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(filePath));
    }

    public String generatePresignedUrl(String key, Duration validDuration) {
        System.out.println("key: "+key);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key("videos/" + key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(validDuration)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    public File generateThumbnail(File video, String outputImagePath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",
                "-i", video.getAbsolutePath(),
                "-ss", "00:00:01",
                "-vframes", "1",
                outputImagePath
        );
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if(exitCode != 0) {
            throw new RuntimeException("Thumbnail creation failed");
        }
        return new File(outputImagePath);
    }

    public void uploadThumbnail(String key, File filePath) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key("thumbnails/" + key)
                .contentType("image/jpeg")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(filePath));
    }
}

