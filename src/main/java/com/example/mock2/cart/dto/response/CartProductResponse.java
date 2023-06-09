package com.example.mock2.cart.dto.response;

import com.example.mock2.cart.model.CartProduct;
import com.example.mock2.product.dto.response.ProductResponse;
import com.example.mock2.product.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartProductResponse {
    ProductResponse product;
    Integer quantity;

    public static CartProductResponse of(CartProduct cartProduct) {
        return new CartProductResponse()
                .setProduct(ProductResponse.of(cartProduct.getProduct()))
                .setQuantity(cartProduct.getQuantity());
    }
}