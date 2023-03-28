package com.example.mock2.user.model;


import com.example.mock2.order_history.model.OrderHistory;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.mock2.cart.model.Cart;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.example.mock2.gallery.model.Gallery;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


import java.util.ArrayList;

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    String username;

    @Column
    String password;

    @Column(length = 50)
    private String address;

    @Column
    private LocalDate birthdate;

    @Enumerated
    @Column(columnDefinition = "tinyint(1)")
    private UserSexEnum sex;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderHistory> orderHistories = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Gallery> galleries;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    public User setCart(Cart cart) {
        cart.setUser(this);
        this.cart = cart;
        return this;
    }
}
