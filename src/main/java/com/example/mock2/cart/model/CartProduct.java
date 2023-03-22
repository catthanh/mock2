package com.example.mock2.cart.model;

import com.example.mock2.product.model.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
public class CartProduct {
    @EmbeddedId
    private CartProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartId")
    @JsonManagedReference
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JsonManagedReference
    private Product product;

    @Column
    private Integer quantity;

    public CartProduct(Cart cart, Product product, Integer quantity) {
        this.cart = cart;
        this.product = product;
        this.id = new CartProductId(cart.getId(), product.getId());
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CartProduct that = (CartProduct) o;
        return Objects.equals(cart, that.cart) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cart, product);
    }
}
