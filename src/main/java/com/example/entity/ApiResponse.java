package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ApiResponse<T> {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime timestamp;

  private int status;
  private String message;
  private T data;

  public ApiResponse(LocalDateTime timestamp, int status, String message, T data) {
    this.timestamp = timestamp;
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public ApiResponse(int status, String message, T data) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public ApiResponse(int status, String message) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.message = message;
    this.data = null;
  }

  // Getters and Setters
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
