package com.example.errors;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(String message) { super(message); }
}