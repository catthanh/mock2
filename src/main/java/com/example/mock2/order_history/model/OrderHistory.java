package com.example.mock2.order_history.model;

import com.example.mock2.cart.model.CartProduct;
import com.example.mock2.user.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_history")
@Getter
@Setter
@NoArgsConstructor
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    private String items;
    private double total;
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatusEnum = OrderStatusEnum.PENDING_PURCHASE;


}
