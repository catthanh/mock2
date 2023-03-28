package com.example.mock2.order_history.model;

import java.util.Map;

import com.example.mock2.order_history.ItemsConverter;
import com.example.mock2.user.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Convert(converter = ItemsConverter.class)
    private Map<String, Integer> items;
    private double total;
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatusEnum = OrderStatusEnum.PENDING_PURCHASE;


}
