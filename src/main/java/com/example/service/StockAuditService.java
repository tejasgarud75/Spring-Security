package com.example.service;


import com.example.entity.Product;
import com.example.entity.StockAudit;
import com.example.entity.enums.StockOperation;
import com.example.repository.ProductRepository;
import com.example.repository.StockAuditRepository;
import com.example.service.dto.CreateProductRequest;
import com.example.service.dto.ProductDTO;
import com.example.service.dto.StockUpdateRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// StockAuditService.java
@Service
@Transactional
public class StockAuditService {

  private final StockAuditRepository stockAuditRepository;
  private final UserService userService;

  public StockAuditService(StockAuditRepository stockAuditRepository,
                           UserService userService) {
    this.stockAuditRepository = stockAuditRepository;
    this.userService = userService;
  }

  public void auditStockOperation(Product product, Integer quantity,
                                  StockOperation operation, String notes) {
    StockAudit audit = new StockAudit();
    audit.setProduct(product);
    audit.setQuantity(quantity);
    audit.setOperation(operation);
    audit.setNotes(notes);

    stockAuditRepository.save(audit);
  }

  public List<StockAudit> getAuditHistoryByProductId(Long productId) {
      return stockAuditRepository.findByProductIdOrderByCreatedAtDesc(productId);
  }

}