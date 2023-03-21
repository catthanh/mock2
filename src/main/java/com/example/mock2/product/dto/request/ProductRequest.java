package com.example.mock2.product.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class ProductRequest {
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin("0.0")
    private double price;

    @NotNull(message = "Quantity is required")
    @Range(min = 0l, message = "Please select positive numbers Only")
    private int quantity;
}
