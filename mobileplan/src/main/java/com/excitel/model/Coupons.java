package com.excitel.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@DynamoDBTable(tableName = "coupon-table")
public class Coupons {

    @DynamoDBRangeKey
    @DynamoDBAttribute(attributeName = "CouponID")
    private String couponId;

    @DynamoDBAttribute(attributeName = "TotalData")
    private String data;

    @NotBlank
    @NotNull
    @DynamoDBAttribute(attributeName = "Expire")
    private String expire;

    @NotBlank
    @NotNull
    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = "CouponType")
    private String type;

//    @DynamoDBAttribute(attributeName = "Url") // NOSONAR
//    private String url; // NOSONAR

    @NotNull
    @DynamoDBAttribute(attributeName = "Limit")
    private String limit;

    @NotNull
    @DynamoDBAttribute(attributeName = "CouponCode")
    private String couponCode;
    @NotNull
    @DynamoDBAttribute(attributeName = "Description")
    private String description;

    @DynamoDBAttribute(attributeName = "Url")
    private String url;

    @DynamoDBAttribute(attributeName = "Image")
    private String image;

}



