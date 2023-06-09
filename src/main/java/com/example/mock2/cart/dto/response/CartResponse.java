package com.example.mock2.cart.dto.response;

import com.example.mock2.cart.model.Cart;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartResponse {
    Integer id;
    List<CartProductResponse> items;

    public static CartResponse of(Cart cart) {
        return new CartResponse()
                .setId(cart.getId())
                .setItems(cart.getCartProducts().stream().map(CartProductResponse::of).collect(Collectors.toList()));
    }
}
