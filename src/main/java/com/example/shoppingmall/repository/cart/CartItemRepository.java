package com.example.shoppingmall.repository.cart;

import com.example.shoppingmall.entity.cart.Cart;
import com.example.shoppingmall.entity.cartItem.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart(Cart cart);
}
