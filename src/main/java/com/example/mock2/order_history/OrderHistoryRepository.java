package com.example.mock2.order_history;

import com.example.mock2.order_history.model.OrderHistory;
import com.example.mock2.order_history.model.OrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Integer> {
    @Query(value = "select cp.quantity from user u " +
            "join cart c on u.id = c.user_id " +
            "join cart_product cp on c.id = cp.cart_id " +
            "where u.id = ?1 order by cp.product_id asc", nativeQuery = true)
    List<Integer> getQuantity(int id);

    @Query(value = "select p.price from user u " +
            "join cart c on u.id = c.user_id " +
            "join cart_product cp on c.id = cp.cart_id " +
            "join product p on p.id = cp.product_id where u.id = ?1" , nativeQuery = true)
    List<Double> getPrice(int id);

    @Query(value = "select p.quantity from user u " +
            "join cart c on u.id = c.user_id " +
            "join cart_product cp on c.id = cp.cart_id " +
            "join product p on p.id = cp.product_id where u.id = ?1 order by cp.product_id asc" , nativeQuery = true)
    List<Integer> getRemainingQuantity(int id);
    @Query(value = "select cp.product_id from user u " +
            "join cart c on u.id = c.user_id " +
            "join cart_product cp on c.id = cp.cart_id where u.id = ?1", nativeQuery = true)
    List<Integer> getProductId(int id);

    @Modifying
    @Transactional
    @Query(value = "update product set quantity = ?1 where id = ?2", nativeQuery = true)
    int calculateRemainingQuantity(int remainingQuantity, int id);

    @Query(value = "select distinct cp.cart_id from user u " +
            "join cart c on u.id = c.user_id " +
            "join cart_product cp on c.id = cp.cart_id where u.id=?1", nativeQuery = true)
    int getCartId(int id);

    @Query(value = "select cp.product_id, cp.quantity from user u " +
            "join cart c on u.id = c.user_id " +
            "join cart_product cp on c.id = cp.cart_id where u.id = ?1", nativeQuery = true)
    List<List<Integer>> getCartProduct(int id);
    @Modifying
    @Transactional
    @Query(value = "delete from cart_product where cart_id = ?1", nativeQuery = true)
    void clearCart(int id);

    @Query(value = "select cart_id from cart_product cp join cart c on cp.cart_id = c.id where c.id=?1", nativeQuery = true)
    Integer getCartProductId(int id);

    Page<OrderHistory> findByUserId(Pageable pageable, int id);
}
