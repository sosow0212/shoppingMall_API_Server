package com.example.shoppingmall.service.product;

import com.example.shoppingmall.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall.dto.product.ProductEditRequestDto;
import com.example.shoppingmall.dto.product.ProductFindAllResponseDto;
import com.example.shoppingmall.dto.product.ProductFindResponseDto;
import com.example.shoppingmall.entity.member.Member;
import com.example.shoppingmall.entity.product.Product;
import com.example.shoppingmall.exception.MemberNotEqualsException;
import com.example.shoppingmall.exception.ProductNotFoundException;
import com.example.shoppingmall.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public void create(ProductCreateRequestDto req, Member member) {
        Product product = new Product(req.getName(), req.getComment(), req.getPrice(), req.getQuantity(), member);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<ProductFindAllResponseDto> findAll() {
        List<Product> products = productRepository.findAll();

        List<ProductFindAllResponseDto> result = new ArrayList<>();

        for(Product product : products) {
            result.add(ProductFindAllResponseDto.toDto(product));
        }

        return result;
    }

    @Transactional(readOnly = true)
    public ProductFindResponseDto find(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return ProductFindResponseDto.toDto(product);
    }


    // 수정 -> 더티체킹
    @Transactional
    public void edit(Long id, ProductEditRequestDto req, Member member) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        if(!member.equals(product.getSeller())) {
            throw new MemberNotEqualsException();
        }

        product.setName(req.getName());
        product.setComment(req.getComment());
        product.setPrice(req.getPrice());
        product.setQuantity(req.getQuantity());
    }

    @Transactional
    public void delete(Long id, Member member) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        if(!member.equals(product.getSeller())) {
            throw new MemberNotEqualsException();
        }
        productRepository.delete(product);
    }
}
