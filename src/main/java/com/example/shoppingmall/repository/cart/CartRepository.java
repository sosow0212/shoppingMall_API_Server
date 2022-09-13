package com.example.shoppingmall.repository.cart;

import com.example.shoppingmall.entity.cart.Cart;
import com.example.shoppingmall.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findCartByMember(Member member);
}
