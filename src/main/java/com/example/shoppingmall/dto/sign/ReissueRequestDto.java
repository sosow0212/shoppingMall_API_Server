package com.example.shoppingmall.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReissueRequestDto {
    private String accessToken;
    private String refreshToken;
}
