package com.example.repository;


import com.example.entity.FileUpload;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends
    JpaRepository<FileUpload, Long> {

}

