package com.excitel.dto;

import com.excitel.model.Coupons;
import lombok.*;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDTO {

    private HttpStatus status;
    private Coupons coupon;
}
