package com.example.shoppingmall.controller;

import com.example.shoppingmall.controller.cart.CartController;
import com.example.shoppingmall.dto.cart.CartCreateRequestDto;
import com.example.shoppingmall.entity.member.Member;
import com.example.shoppingmall.repository.member.MemberRepository;
import com.example.shoppingmall.service.cart.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static com.example.shoppingmall.factory.MemberFactory.createUser;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CartControllerUnitTest {

    @InjectMocks
    CartController cartController;

    @Mock
    CartService cartService;

    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    @DisplayName("장바구니 담기")
    public void createTest() throws Exception {
        // given
        CartCreateRequestDto req = new CartCreateRequestDto(1L, 10);

        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        // when
        mockMvc.perform(
                post("/api/carts")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

        // then
        verify(cartService).create(req, member);
    }

    @Test
    @DisplayName("장바구니 조회")
    public void findAllTest() throws Exception {
        // given
        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        mockMvc.perform(
                get("/api/carts")
        ).andExpect(status().isOk());

        // then
        verify(cartService).findAll(member);
    }

    @Test
    @DisplayName("장바구니 품목 단건 삭제")
    public void deleteByIdTest() throws Exception {
        // given
        Long id = 1L;

        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        mockMvc.perform(
                delete("/api/carts/{cartItemId}", id)
        ).andExpect(status().isOk());

        // then
        verify(cartService).deleteById(id, member);
    }

    @Test
    @DisplayName("장바구니 물건 전체 조회")
    public void buyingAllTest() throws Exception {
        // given
        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        mockMvc.perform(
                post("/api/carts/buying")
        ).andExpect(status().isOk());

        // then
        verify(cartService).buyingAll(member);
    }
}
