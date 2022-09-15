package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.cart.CartCreateRequestDto;
import com.example.shoppingmall.dto.cart.CartItemResponseDto;
import com.example.shoppingmall.entity.cart.Cart;
import com.example.shoppingmall.entity.cartItem.CartItem;
import com.example.shoppingmall.entity.member.Member;
import com.example.shoppingmall.entity.product.Product;
import com.example.shoppingmall.repository.cart.CartItemRepository;
import com.example.shoppingmall.repository.cart.CartRepository;
import com.example.shoppingmall.repository.product.ProductRepository;
import com.example.shoppingmall.service.cart.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.shoppingmall.factory.CartFactory.createCart;
import static com.example.shoppingmall.factory.CartFactory.createCartItem;
import static com.example.shoppingmall.factory.MemberFactory.createSeller;
import static com.example.shoppingmall.factory.MemberFactory.createUser;
import static com.example.shoppingmall.factory.ProductFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartServiceUnitTest {
    @InjectMocks
    CartService cartService;

    @Mock
    CartRepository cartRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("카드 담기")
    public void createTest() {
        // given
        Member member = createUser();
        Member seller = createSeller();
        CartCreateRequestDto req = new CartCreateRequestDto(1L, 10);
        Product product = createProduct(seller);
        Cart cart = createCart(member);

        given(productRepository.findById(req.getProduct_id())).willReturn(Optional.of(product));
        given(cartRepository.findCartByMember(member)).willReturn(Optional.of(cart));
        given(cartRepository.findCartByMember(member)).willReturn(Optional.of(cart));

        // when
        cartService.create(req, member);

        // then
        verify(cartItemRepository).save(any());
    }

    @Test
    @DisplayName("카트 찾기")
    public void findAllTest() {
        // given
        Member seller = createSeller();
        Member member = createUser();
        Cart cart = createCart(member);
        List<CartItem> items = List.of(createCartItem(cart, createProduct(seller)));
        given(cartRepository.findCartByMember(member)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllByCart(cart)).willReturn(items);

        // when
        List<CartItemResponseDto> result = cartService.findAll(member);

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("아이템 삭제")
    public void deleteByIdTest() {
        // given
        Long id = 1L;
        Member member = createUser();
        Member seller = createSeller();
        Cart cart = createCart(member);
        Product product = createProduct(seller);
        CartItem cartItem = createCartItem(cart, product);

        given(cartItemRepository.findById(id)).willReturn(Optional.of(cartItem));

        // when
        cartService.deleteById(id, member);

        // then
        verify(cartItemRepository).delete(cartItem);
    }


    @Test
    @DisplayName("모두 구매")
    public void buyingAllTest() {
        // given
        Member member = createUser();
        member.setMoney(10000000);
        Member seller = createSeller();
        Cart cart = createCart(member);
        Product product = createProduct(seller);
        List<CartItem> cartItems = List.of(createCartItem(cart, product));

        given(cartRepository.findCartByMember(member)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllByCart(cart)).willReturn(cartItems);

        // when
        cartService.buyingAll(member);

        // then
        verify(cartRepository).delete(any());
    }
}
