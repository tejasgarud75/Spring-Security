package com.example.repository;

import com.example.entity.Product;
import com.example.entity.StockAudit;
import com.example.entity.enums.StockOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAuditRepository extends JpaRepository<StockAudit, Long> {

    List<StockAudit> findByProductIdOrderByCreatedAtDesc(Long productId);

    /*@Query("SELECT sa FROM StockAudit sa WHERE sa.product.id = :productId AND sa.operation = :operation ORDER BY sa.performedAt DESC")
    List<StockAudit> findByProductIdAndOperationOrderByPerformedAtDesc(
            @Param("productId") Long productId,
            @Param("operation") StockOperation operation
    );*/
}