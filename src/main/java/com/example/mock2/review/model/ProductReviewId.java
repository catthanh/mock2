package com.example.mock2.review.model;

import java.io.Serializable;
import java.util.Objects;

import com.example.mock2.product.model.Product;
import com.example.mock2.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewId implements Serializable{
  private Product product;
  private User user;

  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProductReviewId)) {
            return false;
        }
        ProductReviewId productReviewId = (ProductReviewId) o;
        return Objects.equals(product, productReviewId.product) && Objects.equals(user, productReviewId.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(product, user);
  }

}
