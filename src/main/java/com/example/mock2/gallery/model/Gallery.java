package com.example.mock2.gallery.model;

import org.hibernate.annotations.DynamicUpdate;

import com.example.mock2.auditable.Auditable;
import com.example.mock2.product.model.Product;
import com.example.mock2.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gallery")
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
public class Gallery extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;
    private String name;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public Gallery(String path, String name, String createdBy, String modifiedBy) {
        super(createdBy, modifiedBy);
        this.path = path;
        this.name = name;
    }
}
