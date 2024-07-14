package com.excitel.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RequestDTO {

    private String active;
    private String planId;
    private String type = "Prepaid";
    private String category;
    private String data;
    private String days;
    private String price;
    private Integer offset = 0;
    private Integer limit = 10;
}
