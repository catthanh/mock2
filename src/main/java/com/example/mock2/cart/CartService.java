package com.example.mock2.cart;

import com.example.mock2.cart.dto.request.AddProductRequest;
import com.example.mock2.cart.dto.request.RemoveProductRequest;
import com.example.mock2.cart.dto.request.UpdateProductQuantityRequest;
import com.example.mock2.cart.model.Cart;
import com.example.mock2.cart.model.CartProduct;
import com.example.mock2.cart.model.CartProductId;
import com.example.mock2.common.dto.request.PaginationQuery;
import com.example.mock2.common.exception.CommonLogicException;
import com.example.mock2.common.exception.NotFoundException;
import com.example.mock2.product.ProductRepository;
import com.example.mock2.product.model.Product;
import com.example.mock2.security.config.AuthenticationPrinciple;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    public CartProduct addProduct(AddProductRequest addProductRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrinciple authenticationPrinciple = (AuthenticationPrinciple) authentication.getPrincipal();
        Optional<Cart> cart = cartRepository.findById(authenticationPrinciple.getId());
        if (cart.isEmpty()) {
            throw new NotFoundException("Cart not found");
        }
        if (addProductRequest.getQuantity() <= 0) {
            throw new NotFoundException("Quantity must be greater than 0");
            }
            Optional<Product> product = productRepository.findById(addProductRequest.getProductId());
            if (product.isEmpty()) {
                throw new NotFoundException("Product not found");
            }
        Optional<CartProduct> cartProduct = cartProductRepository.findByCartAndProduct(cart.get(), product.get());
        if (cartProduct.isPresent()) {
            if (product.get().getQuantity() < cartProduct.get().getQuantity() + addProductRequest.getQuantity()) {
                throw new CommonLogicException("Quantity must be less than or equal to product quantity");
            }
            cartProduct.get().setQuantity(cartProduct.get().getQuantity() + addProductRequest.getQuantity());
            return cartProductRepository.save(cartProduct.get());
        } else {
            if (product.get().getQuantity() < addProductRequest.getQuantity()) {
                throw new CommonLogicException("Quantity must be less than or equal to product quantity");
            }

            CartProduct newCartProduct = new CartProduct(cart.get(), product.get(), addProductRequest.getQuantity());
            return cartProductRepository.save(newCartProduct);
        }
    }

    public void removeProduct(RemoveProductRequest removeProductRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrinciple authenticationPrinciple = (AuthenticationPrinciple) authentication.getPrincipal();
        Optional<Cart> cart = cartRepository.findById(authenticationPrinciple.getId());
        if (cart.isEmpty()) {
            throw new NotFoundException("Cart not found");
        }
        Optional<Product> product = productRepository.findById(removeProductRequest.getProductId());
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        Optional<CartProduct> cartProduct = cartProductRepository.findByCartAndProduct(cart.get(), product.get());
        if (cartProduct.isEmpty()) {
            throw new NotFoundException("Product not found in cart");
        }
        cartProductRepository.delete(cartProduct.get());
    }

    public CartProduct updateProductQuantity(UpdateProductQuantityRequest updateProductQuantityRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrinciple authenticationPrinciple = (AuthenticationPrinciple) authentication.getPrincipal();
        Optional<Cart> cart = cartRepository.findById(authenticationPrinciple.getId());
        if (cart.isEmpty()) {
            throw new NotFoundException("Cart not found");
        }
        if (updateProductQuantityRequest.getQuantity() <= 0) {
            throw new NotFoundException("Quantity must be greater than 0");
        }
        Optional<Product> product = productRepository.findById(updateProductQuantityRequest.getProductId());
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        Optional<CartProduct> cartProduct = cartProductRepository.findByCartAndProduct(cart.get(), product.get());
        if (Objects.equals(updateProductQuantityRequest.getQuantity(), cartProduct.get().getQuantity())) {
            return cartProduct.get();
        } else {
            if (product.get().getQuantity() < updateProductQuantityRequest.getQuantity()) {
                throw new CommonLogicException("Quantity must be less than or equal to product quantity");
            }
            cartProduct.get().setQuantity(updateProductQuantityRequest.getQuantity());
            return cartProductRepository.save(cartProduct.get());
        }
    }

    Page<CartProduct> getCartProducts(PaginationQuery paginationQuery) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrinciple authenticationPrinciple = (AuthenticationPrinciple) authentication.getPrincipal();
        Optional<Cart> cart = cartRepository.findById(authenticationPrinciple.getId());
        if (cart.isEmpty()) {
            throw new NotFoundException("Cart not found");
        }
        Pageable pageable = PageRequest.of(paginationQuery.getPageRequest().getPageNumber(), paginationQuery.getPageRequest().getPageSize());
        return cartProductRepository.findAllByCart(cart.get(), pageable);

    }

}
