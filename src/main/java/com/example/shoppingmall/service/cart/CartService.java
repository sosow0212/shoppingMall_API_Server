package com.example.shoppingmall.service.cart;

import com.example.shoppingmall.dto.cart.CartCreateRequestDto;
import com.example.shoppingmall.dto.cart.CartItemResponseDto;
import com.example.shoppingmall.entity.cart.Cart;
import com.example.shoppingmall.entity.cartItem.CartItem;
import com.example.shoppingmall.entity.member.Member;
import com.example.shoppingmall.entity.product.Product;
import com.example.shoppingmall.exception.*;
import com.example.shoppingmall.repository.cart.CartItemRepository;
import com.example.shoppingmall.repository.cart.CartRepository;
import com.example.shoppingmall.repository.member.MemberRepository;
import com.example.shoppingmall.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void create(CartCreateRequestDto req, Member member) {
        Product product = productRepository.findById(req.getProduct_id()).orElseThrow(ProductNotFoundException::new);

        if (product.getQuantity() < req.getQuantity()) {
            throw new LakingOfProductQuantity();
        }

        // 3. 장바구니 만들어줘야한다 사용자한테

        if (cartRepository.findCartByMember(member).isEmpty()) {
            // 장바구니가 없다면 생성
            Cart cart = new Cart(member);
            cartRepository.save(cart);
        }

        Cart cart = cartRepository.findCartByMember(member).get();

        CartItem cartItem = new CartItem(cart, product, req.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponseDto> findAll(Member member) {
        Cart cart = cartRepository.findCartByMember(member).orElseThrow(CartNotFoundException::new);

        List<CartItem> items = cartItemRepository.findAllByCart(cart);
        List<CartItemResponseDto> result = new ArrayList<>();

//        items.stream().forEach(cartItem -> {
//            result.add(new CartItemResponseDto().toDto(cartItem, cartItem.getProduct()));
//        };

        for(CartItem item : items) {
            Product product = item.getProduct();
            result.add(new CartItemResponseDto().toDto(item, product.getName(), product.getPrice()));
        }
        return result;
    }

    @Transactional
    public void deleteById(Long id, Member member) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(CartItemNotFoundException::new);
        Cart cart = cartItem.getCart();

        if (!cart.getMember().equals(member)) {
            throw new MemberNotEqualsException();
        }

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void buyingAll(Member member) {
        Cart cart = cartRepository.findCartByMember(member).orElseThrow(CartNotFoundException::new);
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

        cartItems.stream().forEach(cartItem -> {
            Product product = cartItem.getProduct();

            checkMemberCanBuyCartItemForEach(product, member, cartItem);

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            member.setMoney(member.getMoney() - product.getPrice() * cartItem.getQuantity());
            product.getSeller().setMoney(product.getSeller().getMoney() + (product.getPrice() * cartItem.getQuantity()));
        });

        checkMemberCanBuyCartItemAll(member);
        cartRepository.delete(cart);
    }

    public boolean checkMemberCanBuyCartItemForEach(Product product, Member member, CartItem cartItem) {
        // 1. 구매 수량 체크
        if (cartItem.getQuantity() > product.getQuantity()) {
            throw new LakingOfProductQuantity();
        }

        // 2. 사용자 돈 있는지 체크
        if (member.getMoney() < product.getPrice() * cartItem.getQuantity()) {
            throw new UserLackOfMoneyException();
        }

        return true;
    }

    public boolean checkMemberCanBuyCartItemAll(Member member) {
        if (member.getMoney() < 0) {
            throw new UserLackOfMoneyException();
        }

        return true;
    }
}
