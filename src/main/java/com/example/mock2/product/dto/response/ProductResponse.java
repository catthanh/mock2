package com.example.mock2.product.dto.response;

import com.example.mock2.product.model.Product;
import com.example.mock2.review.dto.response.ReviewResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ProductResponse {
    private Integer id;
    private String name;
    private double price;
    private int quantity;
    private ReviewResponse review;
    public static ProductResponse of(Product product){
        return new ProductResponse()
                .setId(product.getId())
                .setName(product.getName())
                .setPrice(product.getPrice())
                .setQuantity(product.getQuantity())
                .setReview(ReviewResponse.builder().star(product.getReviewScore()).build());
    }
}
