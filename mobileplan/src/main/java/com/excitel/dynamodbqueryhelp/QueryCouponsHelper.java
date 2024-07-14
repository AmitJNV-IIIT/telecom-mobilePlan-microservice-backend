package com.excitel.dynamodbqueryhelp;


import com.excitel.model.Coupons;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.dynamodb.model.*;


import java.util.*;

@Component
public class QueryCouponsHelper {

    private static final String COUPONTYPE = "CouponType";
    private static final String COUPONID = "CouponID";
    private static final String TABLE_NAME = "coupon-table";


    public QueryRequest getAllCouponsByType(String couponType) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":CouponType", AttributeValue.builder().s(couponType).build());

        return QueryRequest.builder()
                .tableName(TABLE_NAME)
                .keyConditionExpression("CouponType = :CouponType")
                .expressionAttributeValues(expressionAttributeValues)
                .build();
    }


    /**
     * Creates a GetItemRequest to retrieve a coupon by its ID and type.
     *
     * @param couponType The type of the coupon.
     * @param couponId   The ID of the coupon.
     * @return           GetItemRequest object for retrieving the coupon.
     */

    public GetItemRequest getItemByCouponId(String couponType, String couponId){
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(COUPONTYPE, AttributeValue.builder().s(couponType).build());
        key.put(COUPONID, AttributeValue.builder().s(couponId).build());

        return GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();
    }

    /**
     * Creates a GetItemRequest to retrieve coupon data by mobile number.
     *
     * @param mobileNumber The mobile number associated with the coupon.
     * @return             GetItemRequest object for retrieving the coupon.
     */

    public GetItemRequest getItemByMobileNumber(String mobileNumber){
        Map<String,AttributeValue> key=new HashMap<>();
        key.put("MobileNumber", AttributeValue.builder().s(mobileNumber).build());
        return GetItemRequest.builder()
                .tableName("customer-coupon-table")
                .key(key)
                .build();
    }
    /**
     * Creates a PutItemRequest to add a new coupon.
     *
     * @param coupon The coupon object to be added.
     * @return       PutItemRequest object for adding the coupon.
     */
    public PutItemRequest addCouponQuery(Coupons coupon){

        Map<String, AttributeValue> item = new HashMap<>();

        item.put(COUPONTYPE, AttributeValue.builder().s(coupon.getType()).build());
        item.put(COUPONID, AttributeValue.builder().s(coupon.getCouponId()).build());

        item.put("CouponCode",AttributeValue.builder().s(!Objects.isNull(coupon.getCouponCode())? coupon.getCouponCode():"null").build());
        item.put("Expire",AttributeValue.builder().s(!Objects.isNull(coupon.getExpire())?coupon.getExpire():"null").build());
        item.put("Limit",AttributeValue.builder().s(!Objects.isNull(coupon.getLimit())?coupon.getLimit():"null").build());
        item.put("Description",AttributeValue.builder().s(!Objects.isNull(coupon.getDescription())?coupon.getDescription():"null").build());
        item.put("TotalData",AttributeValue.builder().s(!Objects.isNull(coupon.getData())?coupon.getData():"null").build());
        item.put("Image",AttributeValue.builder().s(!Objects.isNull(coupon.getImage())?coupon.getImage():"null").build());
        return PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .conditionExpression("attribute_not_exists(CouponType) AND attribute_not_exists(CouponID)")
                .item(item)
                .build();
    }
    /**
     * Creates an UpdateItemRequest to update a coupon.
     *
     * @param coupon   The updated coupon object.
     * @param couponId The ID of the coupon to be updated.
     * @return         UpdateItemRequest object for updating the coupon.
     */
    public UpdateItemRequest updateQuery(Coupons coupon, String couponId){
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(COUPONTYPE, AttributeValue.builder().s(coupon.getType()).build());
        key.put(COUPONID, AttributeValue.builder().s(couponId).build());


        Map<String, AttributeValueUpdate> item = new HashMap<>();
        item.put("CouponCode", AttributeValueUpdate.builder().value(AttributeValue.builder().s(coupon.getCouponCode()).build()).build());//NOSONAR
        item.put("Expire", AttributeValueUpdate.builder().value(AttributeValue.builder().s(coupon.getExpire()).build()).build());//NOSONAR
        item.put("Image", AttributeValueUpdate.builder().value(AttributeValue.builder().s(coupon.getImage()).build()).build());//NOSONAR
        item.put("Limit", AttributeValueUpdate.builder().value(AttributeValue.builder().s(coupon.getLimit()).build()).build());//NOSONAR
        item.put("TotalData", AttributeValueUpdate.builder().value(AttributeValue.builder().s(coupon.getData()).build()).build());//NOSONAR
        item.put("Description", AttributeValueUpdate.builder().value(AttributeValue.builder().s(coupon.getDescription()).build()).build());//NOSONAR

        return UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .attributeUpdates(item)
                .build();
    }
    /**
     * Creates a DeleteItemRequest to delete a coupon by its ID and type.
     *
     * @param couponId   The ID of the coupon to be deleted.
     * @param couponType The type of the coupon to be deleted.
     * @return           DeleteItemRequest object for deleting the coupon.
     */
    public DeleteItemRequest deleteCouponQuery(String couponId, String couponType){

        Map<String, AttributeValue> key = new HashMap<>();
        key.put(COUPONTYPE, AttributeValue.builder().s(couponType).build());
        key.put(COUPONID, AttributeValue.builder().s(couponId).build());

        return DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();
    }

    /**
     * Creates a GetItemRequest to retrieve coupon data by its ID and type.
     *
     * @param couponId   The ID of the coupon.
     * @param couponType The type of the coupon.
     * @return           GetItemRequest object for retrieving the coupon data.
     */

    public GetItemRequest getCouponDataByCouponId(String couponId,String couponType) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(COUPONID, AttributeValue.builder().s(couponId).build());
        key.put(COUPONTYPE,AttributeValue.builder().s(couponType).build());

        return GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();
    }
}

