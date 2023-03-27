package com.example.mock2.product.model;

import java.util.ArrayList;
import java.util.List;

import com.example.mock2.cart.model.CartProduct;
import com.example.mock2.gallery.model.Gallery;
import com.example.mock2.review.model.ProductReview;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private double price;
    private double reviewScore;
    private int quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Gallery> galleries;

    @OneToMany(mappedBy = "product")
    private List<ProductReview> reviews;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void setReviewScore() {
        double averageStar = 0;
        for (ProductReview review : reviews) {
            averageStar = review.getStar() + averageStar;
        }
        averageStar = averageStar / (double) reviews.size();
        this.reviewScore = Math.round(averageStar * 10.0) / 10.0;
    }

}
