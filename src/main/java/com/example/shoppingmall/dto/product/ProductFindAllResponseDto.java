package com.example.shoppingmall.dto.product;

import com.example.shoppingmall.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFindAllResponseDto {
    private String name;
    private int price;
    private String seller_username;
    private LocalDateTime createdAt;

    public static ProductFindAllResponseDto toDto(Product p) {
        return new ProductFindAllResponseDto(p.getName(), p.getPrice(), p.getSeller().getName(), p.getCreatedAt());
    }
}
