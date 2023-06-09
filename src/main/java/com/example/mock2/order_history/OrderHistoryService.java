package com.example.mock2.order_history;

import com.example.mock2.cart.CartRepository;
import com.example.mock2.cart.model.Cart;
import com.example.mock2.common.dto.request.PaginationQuery;
import com.example.mock2.common.exception.NotFoundException;
import com.example.mock2.common.exception.ProductAvailableException;
import com.example.mock2.order_history.model.OrderHistory;
import com.example.mock2.order_history.model.OrderStatusEnum;
import com.example.mock2.product.ProductRepository;
import com.example.mock2.security.config.AuthenticationPrinciple;
import com.example.mock2.user.UserRepository;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderHistory createInvoice(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrinciple authenticationPrinciple = (AuthenticationPrinciple) authentication.getPrincipal();
        Optional<Cart> cart = cartRepository.findById(authenticationPrinciple.getId());
        if (cart.isEmpty()) {
            throw new NotFoundException("Cart not found");
        } else {
            int id = authenticationPrinciple.getId();
            Integer cart_product = orderHistoryRepository.getCartProductId(id);
            if (cart_product == null){
                throw new NotFoundException("Cart is empty");
            }
            List<Integer> quantityList = orderHistoryRepository.getQuantity(id);
            System.out.println(quantityList);
            List<Integer> remainingQuantityList = orderHistoryRepository.getRemainingQuantity(id);
            for (int i=0;i<quantityList.size(); i++){
                if (quantityList.get(i) > remainingQuantityList.get(i)){
                    throw new ProductAvailableException("No products remaining");
                }
            }
            List<Double> priceList = orderHistoryRepository.getPrice(id);
            System.out.println(priceList);
            double price = 0;
            for (int i=0;i<quantityList.size(); i++){
                price += quantityList.get(i)*priceList.get(i);
            }

            List<Integer> productId = orderHistoryRepository.getProductId(id);
            for (int i = 0; i<productId.size(); i++){
                orderHistoryRepository.calculateRemainingQuantity(remainingQuantityList.get(i) - quantityList.get(i), productId.get(i));
            }
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setUser(userRepository.findById(id).get());
            List<List<Integer>> listItems = orderHistoryRepository.getCartProduct(id);
            Map<String, Integer> items = new HashMap();
            for(List<Integer> i : listItems){
//                items += productRepository.findById(i.get(0)).get().getName() + " - " + i.get(1) + "\n";
                items.put(productRepository.findById(i.get(0)).get().getName(), i.get(1));
            }
            orderHistory.setOrderStatusEnum(OrderStatusEnum.SUCCESS);
            orderHistory.setItems(items);
            orderHistory.setTotal(price);
            orderHistoryRepository.clearCart(orderHistoryRepository.getCartId(id));
            return orderHistoryRepository.save(orderHistory);
        }
    }

    public Page<OrderHistory> getOrderHistoryForUser(PaginationQuery paginationQuery){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrinciple authenticationPrinciple = (AuthenticationPrinciple) authentication.getPrincipal();
        int id = authenticationPrinciple.getId();

        Pageable pageable = paginationQuery.getPageRequest();
        if (!orderHistoryRepository.findByUserId(pageable, id).hasContent()){
            throw new NotFoundException("You have no order history");
        }
        return orderHistoryRepository.findByUserId(pageable, id);
    }

    public Page<OrderHistory> getAllOrderHistoryForAdmin(PaginationQuery paginationQuery){
        Pageable pageable = paginationQuery.getPageRequest();
        return orderHistoryRepository.findAll(pageable);
    }
}
