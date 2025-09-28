package com.example.service;


import com.amazonaws.services.kms.model.NotFoundException;
import com.example.entity.Product;
import com.example.entity.StockAudit;
import com.example.entity.enums.StockOperation;
import com.example.errors.InsufficientStockException;
import com.example.errors.ProductNotFoundException;
import com.example.repository.ProductRepository;
import com.example.service.dto.CreateProductRequest;
import com.example.service.dto.ProductDTO;
import com.example.service.dto.StockUpdateRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ProductService {

  private final ProductRepository productRepository;
  private final StockAuditService stockAuditService;
  private final UserService userService;

  public ProductService(ProductRepository productRepository,
                        StockAuditService stockAuditService,
                        UserService userService) {
    this.productRepository = productRepository;
    this.stockAuditService = stockAuditService;
    this.userService = userService;
  }

  public CreateProductRequest createProduct(CreateProductRequest request) {
    Product product = new Product();
    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setStockQuantity(request.getStockQuantity());
    product.setLowStockThreshold(request.getLowStockThreshold());

    Product savedProduct = productRepository.save(product);

    // Audit initial stock if any
    if (request.getStockQuantity() > 0) {
      stockAuditService.auditStockOperation(
              savedProduct,
              request.getStockQuantity(),
              StockOperation.PRODUCT_CREATION,
              "Initial stock on product creation"
      );
      request.setId(product.getId());
    }

    return request;
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  public Optional<Product> getProductById(Long id) {
    return productRepository.findById(id);
  }

  public Product  updateProduct(Long id, ProductDTO productDTO) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

    // Track changes for audit if needed
    Integer oldStock = product.getStockQuantity();

    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setLowStockThreshold(productDTO.getLowStockThreshold());

    Product updatedProduct = productRepository.save(product);

    // Audit stock changes if stock was modified
    if (!oldStock.equals(productDTO.getStockQuantity())) {
      int quantityChange = productDTO.getStockQuantity() - oldStock;
      StockOperation operation = quantityChange > 0 ? StockOperation.STOCK_INCREASE : StockOperation.STOCK_DECREASE;

        stockAuditService.auditStockOperation(
              updatedProduct,
              Math.abs(quantityChange),
              operation,
              "Stock updated during product modification"
      );
    }

    return updatedProduct;
  }

  public void deleteProduct(Long id) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

    productRepository.delete(product);
  }

  public Product increaseStock(Long productId, StockUpdateRequest request) {
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));

    product.setStockQuantity(product.getStockQuantity() + request.getQuantity());
    Product updatedProduct = productRepository.save(product);

    stockAuditService.auditStockOperation(
            updatedProduct,
            request.getQuantity(),
            StockOperation.STOCK_INCREASE,
            request.getNotes()
    );

    return updatedProduct;
  }

  public Product decreaseStock(Long productId, StockUpdateRequest request) {
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));

    if (product.getStockQuantity() < request.getQuantity()) {
      throw new InsufficientStockException(
              "Insufficient stock. Available: " + product.getStockQuantity() +
                      ", Requested: " + request.getQuantity()
      );
    }

    product.setStockQuantity(product.getStockQuantity() - request.getQuantity());
    Product updatedProduct = productRepository.save(product);

    stockAuditService.auditStockOperation(
            updatedProduct,
            request.getQuantity(),
            StockOperation.STOCK_DECREASE,
            request.getNotes()
    );

    return updatedProduct;
  }

  public List<Product> getLowStockProducts() {
    return productRepository.findLowStockProducts();
  }

  public List<StockAudit> getProductAuditHistory(Long productId) {
    return stockAuditService.getAuditHistoryByProductId(productId);
  }
}
