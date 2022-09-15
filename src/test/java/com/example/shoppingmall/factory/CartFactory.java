package com.example.shoppingmall.factory;

import com.example.shoppingmall.entity.cart.Cart;
import com.example.shoppingmall.entity.cartItem.CartItem;
import com.example.shoppingmall.entity.member.Member;
import com.example.shoppingmall.entity.product.Product;

public class CartFactory {

    public static Cart createCart(Member member) {
        return new Cart(1L, member);
    }

    public static CartItem createCartItem(Cart cart, Product product) {
        return new CartItem(cart, product, 1);
    }

}
