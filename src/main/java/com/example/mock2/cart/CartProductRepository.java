package com.example.mock2.cart;

import com.example.mock2.cart.model.Cart;
import com.example.mock2.cart.model.CartProduct;
import com.example.mock2.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
    Optional<CartProduct> findByCartAndProduct(Cart cart, Product product);

    Page<CartProduct> findAllByCart(Cart cart, Pageable pageable);
}
