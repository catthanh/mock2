package com.example.mock2.user.model;


import com.example.mock2.cart.model.Cart;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
