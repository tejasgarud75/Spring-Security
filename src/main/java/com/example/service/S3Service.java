package com.example.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

  private final AmazonS3 amazonS3;

  @Value("${aws.s3.bucket-name}")
  private String bucketName;

  public S3Service(AmazonS3 amazonS3) {
    this.amazonS3 = amazonS3;
  }

  public String uploadImage(MultipartFile file) throws IOException {
    String fileKey = UUID.randomUUID() + "_" + file.getOriginalFilename();

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    amazonS3.putObject(new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata));

    return fileKey;
  }

  public byte[] getImageData(String fileName) throws IOException {
    S3Object s3Object = amazonS3.getObject(bucketName, fileName);
    try (InputStream inputStream = s3Object.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
      return outputStream.toByteArray();
    }
  }

}
