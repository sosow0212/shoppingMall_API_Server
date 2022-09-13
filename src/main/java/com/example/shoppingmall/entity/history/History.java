package com.example.shoppingmall.entity.history;

import com.example.shoppingmall.entity.common.EntityDate;
import com.example.shoppingmall.entity.member.Member;
import com.example.shoppingmall.entity.product.Product;
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
public class History extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Product product;
}
