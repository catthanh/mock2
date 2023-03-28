package com.example.mock2.product;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.mock2.common.dto.request.PaginationQuery;
import com.example.mock2.product.dto.request.ProductRequest;
import com.example.mock2.product.model.Product;
import com.example.mock2.security.config.AuthenticationPrinciple;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    public Product addNewProduct(ProductRequest productRequest) {
        Product newProduct = new Product();
        newProduct.setName(productRequest.getName());
        newProduct.setPrice(productRequest.getPrice());
        newProduct.setQuantity(productRequest.getQuantity());
        log.trace("Add new Product {} - {}", newProduct.toString(), getCurrentUser().getUsername());
        return productRepository.save(newProduct);
    }

    public Product getById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Page<Product> getAllProduct(PaginationQuery paginationQuery) {
        Pageable pageable = paginationQuery.getPageRequest();
        return productRepository.findAll(pageable);
    }

    public Product editProduct(ProductRequest productRequest, Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Product not found");
        }

        Product product = optionalProduct.get();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        log.trace("Edit Product {} - {}", product, getCurrentUser().getUsername());
        return productRepository.save(product);
    }

    public Product deleteProduct(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
        log.trace("Delete Product {} - {}", optionalProduct, getCurrentUser().getUsername());
        return optionalProduct.get();
    }

    private AuthenticationPrinciple getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (AuthenticationPrinciple) auth.getPrincipal();
    }
}
