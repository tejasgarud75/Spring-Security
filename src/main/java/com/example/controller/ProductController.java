package com.example.controller;

import com.example.entity.ApiResponse;
import com.example.entity.Product;
import com.example.entity.StockAudit;
import com.example.errors.BadRequestAlertException;
import com.example.errors.ProductNotFoundException;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.service.dto.CreateProductRequest;
import com.example.service.dto.ProductDTO;
import com.example.service.dto.StockUpdateRequest;
import com.example.service.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
// ProductController.java
@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<CreateProductRequest> createProduct(@RequestBody CreateProductRequest request) {
    CreateProductRequest product = productService.createProduct(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(product);
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = productService.getAllProducts();
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    Product product = productService.getProductById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    return ResponseEntity.ok(product);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
    Product product = productService.updateProduct(id, productDTO);
    return ResponseEntity.ok(product);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.ok("Product Deleted");
  }

  @PostMapping("/{id}/increase-stock")
  public ResponseEntity<Product> increaseStock(@PathVariable Long id,@RequestBody StockUpdateRequest request) {
    Product product = productService.increaseStock(id, request);
    return ResponseEntity.ok(product);
  }

  @PostMapping("/{id}/decrease-stock")
  public ResponseEntity<Product> decreaseStock(@PathVariable Long id, @RequestBody StockUpdateRequest request) {
    Product product = productService.decreaseStock(id, request);
    return ResponseEntity.ok(product);
  }

  @GetMapping("/low-stock")
  public ResponseEntity<List<Product>> getLowStockProducts() {
    List<Product> products = productService.getLowStockProducts();
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{id}/audit-history")
  public ResponseEntity<List<StockAudit>> getProductAuditHistory(@PathVariable Long id) {
    List<StockAudit> auditHistory = productService.getProductAuditHistory(id);
    return ResponseEntity.ok(auditHistory);
  }
}
