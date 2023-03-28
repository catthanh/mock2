package com.example.mock2.order_history.dto.response;

import com.example.mock2.order_history.model.OrderHistory;
import com.example.mock2.order_history.model.OrderStatusEnum;
import com.example.mock2.user.dto.response.UserResponse;
import com.example.mock2.user.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OrderHistoryResponse {
    private Integer id;
    private Map<String, Integer> items;
    private double total;
    private OrderStatusEnum orderStatusEnum;
    public static OrderHistoryResponse of(OrderHistory orderHistory){
        return new OrderHistoryResponse()
                .setId(orderHistory.getId())
                .setItems(orderHistory.getItems())
                .setTotal(orderHistory.getTotal())
                .setOrderStatusEnum(orderHistory.getOrderStatusEnum());

    }


}
