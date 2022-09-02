package com.example.shoppingmall.controller.product;

import com.example.shoppingmall.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall.dto.product.ProductEditRequestDto;
import com.example.shoppingmall.entity.member.Member;
import com.example.shoppingmall.exception.MemberNotFoundException;
import com.example.shoppingmall.repository.member.MemberRepository;
import com.example.shoppingmall.response.Response;
import com.example.shoppingmall.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;
    private final MemberRepository memberRepository;

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody ProductCreateRequestDto req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        productService.create(req, member);
        return Response.success();
    }

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public Response findAll() {
        return Response.success(productService.findAll());
    }

    @GetMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response find(@PathVariable("id") Long id) {
        return Response.success(productService.find(id));
    }

    @PutMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response edit(@PathVariable("id") Long id, @Valid @RequestBody ProductEditRequestDto req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        productService.edit(id, req, member);
        return Response.success();
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        productService.delete(id, member);
        return Response.success();
    }

}
