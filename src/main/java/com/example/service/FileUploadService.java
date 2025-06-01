package com.example.service;

import com.example.entity.FileUpload;
import com.example.errors.BadRequestAlertException;
import com.example.repository.FileUploadRepository;

import java.io.IOException;
import java.util.Base64;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

  private final S3Service s3Service;
  private final FileUploadRepository fileUploadRepository;

    public FileUploadService(S3Service s3Service, FileUploadRepository fileUploadRepository) {
        this.s3Service = s3Service;
        this.fileUploadRepository = fileUploadRepository;
    }

    public String uploadFile(MultipartFile image)
      throws IOException, BadRequestAlertException {

    String key = s3Service.uploadImage(image);
    FileUpload fileUpload = new FileUpload();
    fileUpload.setPhotoUrl(key);
    fileUploadRepository.save(fileUpload);

    return "File Uploaded successfully";
  }


  public Optional<String> getFile(Long imageId) throws IOException {

    FileUpload listResponse = fileUploadRepository.findById(imageId)
            .orElseThrow();

      byte[] image = s3Service.getImageData(listResponse.getPhotoUrl());

      return Optional.of(Base64.getEncoder().encodeToString(image));
  }
}
