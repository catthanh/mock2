package com.example.mock2.review.model;

import org.hibernate.annotations.DynamicUpdate;

import com.example.mock2.auditable.Auditable;
import com.example.mock2.product.model.Product;
import com.example.mock2.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "product_review")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DynamicUpdate
@IdClass(ProductReviewId.class)
public class ProductReview extends Auditable<String> {
  @Id
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Id
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private double star = 5;

}
