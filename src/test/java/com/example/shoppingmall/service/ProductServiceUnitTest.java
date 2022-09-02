package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall.dto.product.ProductEditRequestDto;
import com.example.shoppingmall.dto.product.ProductFindAllResponseDto;
import com.example.shoppingmall.dto.product.ProductFindResponseDto;
import com.example.shoppingmall.entity.member.Member;
import com.example.shoppingmall.entity.product.Product;
import com.example.shoppingmall.repository.product.ProductRepository;
import com.example.shoppingmall.service.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.shoppingmall.factory.MemberFactory.createUser;
import static com.example.shoppingmall.factory.ProductFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("상품 생성")
    public void createTest() {
        // given
        ProductCreateRequestDto req = new ProductCreateRequestDto("상품명", "설명", 100, 1);
        Member member = createUser();

        // when
        productService.create(req, member);

        // then
        verify(productRepository).save(any());
    }

    @Test
    @DisplayName("상품 모두 불러오기")
    public void findAllTest() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(createProduct(createUser()));
        products.add(createProduct(createUser()));
        given(productRepository.findAll()).willReturn(products);


        // when
        List<ProductFindAllResponseDto> result = productService.findAll();


        // then
        assertThat(result.size()).isEqualTo(products.size());
    }

    @Test
    @DisplayName("상품 상세 정보 확인")
    public void findTest() {
        // given
        Long id = 1L;
        Product product = createProduct(createUser());

        given(productRepository.findById(id)).willReturn(Optional.of(product));

        // when
        ProductFindResponseDto result = productService.find(id);

        // then
        assertThat(product.getName()).isEqualTo(result.getName());
    }


    @Test
    @DisplayName("상품 수정")
    public void editTest() {
        // given
        Long id = 1L;
        ProductEditRequestDto req = new ProductEditRequestDto("상품명 수정", "설명 수정", 100, 100);
        Member member = createUser();
        Product product = createProduct(member);
        given(productRepository.findById(id)).willReturn(Optional.of(product));

        // when
        productService.edit(id, req, member);

        // then
        assertThat(product.getName()).isEqualTo(req.getName());
    }


    @Test
    @DisplayName("상품 삭제")
    public void deleteTest() {
        // given
        Long id = 1L;
        Member member = createUser();
        Product product = createProduct(member);
        given(productRepository.findById(id)).willReturn(Optional.of(product));

        // when
        productService.delete(id, member);

        // then
        verify(productRepository).delete(product);
    }
}
