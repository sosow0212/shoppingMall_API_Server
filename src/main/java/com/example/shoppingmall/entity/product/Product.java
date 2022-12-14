package com.example.shoppingmall.entity.product;

import com.example.shoppingmall.entity.common.EntityDate;
import com.example.shoppingmall.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member seller;

    public Product(String name, String comment, int price, int quantity, Member seller) {
        this.name = name;
        this.comment = comment;
        this.price = price;
        this.quantity = quantity;
        this.seller = seller;
    }
}
