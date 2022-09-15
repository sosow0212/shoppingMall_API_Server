package com.example.shoppingmall.factory;

import com.example.shoppingmall.entity.member.Member;
import com.example.shoppingmall.entity.product.Product;

public class ProductFactory {

    public static Product createProduct(Member seller) {
        return new Product("name", "comment", 1000, 100, seller);
    }
}
