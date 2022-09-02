package com.example.shoppingmall.controller;

import com.example.shoppingmall.controller.product.ProductController;
import com.example.shoppingmall.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall.dto.product.ProductEditRequestDto;
import com.example.shoppingmall.entity.member.Member;
import com.example.shoppingmall.repository.member.MemberRepository;
import com.example.shoppingmall.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
public class ProductControllerUnitTest {
    @InjectMocks
    ProductController productController;

    @Mock // 가짜로 객체를 주입한다.
    ProductService productService;

    @Mock // 가짜 데이터베이스 주입
    MemberRepository memberRepository;

    MockMvc mockMvc; // 가짜로 요청
    ObjectMapper objectMapper = new ObjectMapper(); // @RequestBody

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("상품 생성")
    public void createTest() throws Exception {
        // given
        // 주어진 값 == 준비물 (매개변수, )
        ProductCreateRequestDto req = new ProductCreateRequestDto("name", "comment", 1, 1);

        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        // 요청 실행해보자
        mockMvc.perform(
                post("/api/products")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());


        // then
        // 요청이 잘 실행 됐는지, 검증하기
        verify(productService).create(req, member);
    }

    @Test
    @DisplayName("목록조회 테스트")
    public void findAllTest() throws Exception {
        // given

        // when
        mockMvc.perform(
                get("/api/products")
        ).andExpect(status().isOk());

        // then
        verify(productService).findAll();
    }


    @Test
    @DisplayName("상품 단건 조회")
    public void findTest() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
                get("/api/products/{id}", id)
        ).andExpect(status().isOk());

        //then
        verify(productService).find(id);
    }

    @Test
    @DisplayName("상품 수정")
    public void editTest() throws Exception {
        // given
        Long id = 1L;
        ProductEditRequestDto req = new ProductEditRequestDto("a", "a", 1, 1);

        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        mockMvc.perform(
                put("/api/products/{id}", id)
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        // then
        verify(productService).edit(id, req, member);
    }


    @Test
    @DisplayName("상품 제거")
    public void deleteTest() throws Exception {
        // given
        Long id = 1L;
        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        // when
        mockMvc.perform(
                delete("/api/products/{id}", id)
        ).andExpect(status().isOk());


        // then
        verify(productService).delete(id, member);
    }


}
