package com.example.mock2.product;

import com.example.mock2.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

  @Query(value = "select name from product where id = ?1", nativeQuery = true)
  String findNameById(Integer productId);
}
