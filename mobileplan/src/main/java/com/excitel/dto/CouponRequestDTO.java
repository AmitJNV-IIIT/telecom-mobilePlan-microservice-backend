package com.excitel.dto;


import lombok.*;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponRequestDTO {
    private List<String> couponIdList;
    private String couponType="internal";
}
