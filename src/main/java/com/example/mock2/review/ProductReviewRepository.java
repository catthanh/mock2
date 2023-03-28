package com.example.mock2.review;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mock2.review.model.ProductReview;
import com.example.mock2.review.model.ProductReviewId;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, ProductReviewId> {

  Optional<ProductReview> findByUserIdAndProductId(Integer id, Integer productId);

}
