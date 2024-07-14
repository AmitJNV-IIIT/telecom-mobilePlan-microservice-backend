package com.excitel.dto;

import com.excitel.model.Coupons;
import lombok.*;
import org.springframework.http.HttpStatus;


import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponseDTO {
    private HttpStatus status;
    private Map<String,Coupons> coupons;
}
