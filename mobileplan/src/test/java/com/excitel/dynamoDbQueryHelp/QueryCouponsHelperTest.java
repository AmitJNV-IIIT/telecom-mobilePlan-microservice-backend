package com.excitel.dynamoDbQueryHelp;

import com.excitel.dynamodbqueryhelp.QueryCouponsHelper;
import com.excitel.model.Coupons;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryCouponsHelperTest {

    @Test
    void getItemByCouponId() {
        QueryCouponsHelper helper = new QueryCouponsHelper();
        GetItemRequest request = helper.getItemByCouponId("type", "123");

        assertEquals("coupon-table", request.tableName());
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("CouponType", AttributeValue.builder().s("type").build());
        key.put("CouponID", AttributeValue.builder().s("123").build());
        assertEquals(key, request.key());
    }

    @Test
    void getItemByMobileNumber() {
        QueryCouponsHelper helper = new QueryCouponsHelper();
        GetItemRequest request = helper.getItemByMobileNumber("1234567890");

        assertEquals("customer-coupon-table", request.tableName());
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("MobileNumber", AttributeValue.builder().s("1234567890").build());
        assertEquals(key, request.key());
    }

    @Test
    void addCouponQuery() {
        QueryCouponsHelper helper = new QueryCouponsHelper();
        Coupons coupon = new Coupons();
        coupon.setType("type");
        coupon.setCouponId("123");
        coupon.setCouponCode("code");
        coupon.setExpire("expiry");
        coupon.setLimit("limit");
        coupon.setData("data");
        coupon.setDescription("description");
        coupon.setImage("image");

        PutItemRequest request = helper.addCouponQuery(coupon);

        assertEquals("coupon-table", request.tableName());
        assertEquals("attribute_not_exists(CouponType) AND attribute_not_exists(CouponID)", request.conditionExpression());

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("CouponType", AttributeValue.builder().s("type").build());
        item.put("CouponID", AttributeValue.builder().s("123").build());
        item.put("CouponCode", AttributeValue.builder().s("code").build());
        item.put("Expire", AttributeValue.builder().s("expiry").build());
        item.put("Limit", AttributeValue.builder().s("limit").build());
        item.put("Description",AttributeValue.builder().s("description").build());
        item.put("TotalData", AttributeValue.builder().s("data").build());
        item.put("Image", AttributeValue.builder().s("image").build());
        assertEquals(item, request.item());
    }

    @Test
    void addCouponQuery_null_fields() {
        QueryCouponsHelper helper = new QueryCouponsHelper();
        Coupons coupon = new Coupons();
        coupon.setType("type");
        coupon.setCouponId("123");
        coupon.setCouponCode(null);
        coupon.setExpire(null);
        coupon.setLimit(null);
        coupon.setData(null);
        coupon.setDescription(null);
        coupon.setImage(null);

        PutItemRequest request = helper.addCouponQuery(coupon);

        assertEquals("coupon-table", request.tableName());
        assertEquals("attribute_not_exists(CouponType) AND attribute_not_exists(CouponID)", request.conditionExpression());

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("CouponType", AttributeValue.builder().s("type").build());
        item.put("CouponID", AttributeValue.builder().s("123").build());
        item.put("CouponCode", AttributeValue.builder().s("null").build());
        item.put("Expire", AttributeValue.builder().s("null").build());
        item.put("Limit", AttributeValue.builder().s("null").build());
        item.put("Description", AttributeValue.builder().s("null").build());
        item.put("TotalData", AttributeValue.builder().s("null").build());
        item.put("Image", AttributeValue.builder().s("null").build());

        assertEquals(item, request.item());
    }

    @Test
    void updateQuery() {
        QueryCouponsHelper helper = new QueryCouponsHelper();
        Coupons coupon = new Coupons();
        coupon.setType("type");
        coupon.setCouponCode("code");
        coupon.setExpire("expiry");
        coupon.setLimit("limit");
        coupon.setDescription("description");
        coupon.setData("data");
        coupon.setImage("image");

        UpdateItemRequest request = helper.updateQuery(coupon, "123");

        assertEquals("coupon-table", request.tableName());

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("CouponType", AttributeValue.builder().s("type").build());
        key.put("CouponID", AttributeValue.builder().s("123").build());
        assertEquals(key, request.key());

        Map<String, AttributeValueUpdate> attributeUpdates = new HashMap<>();
        attributeUpdates.put("CouponCode", AttributeValueUpdate.builder().value(AttributeValue.builder().s("code").build()).build());
        attributeUpdates.put("Expire", AttributeValueUpdate.builder().value(AttributeValue.builder().s("expiry").build()).build());
        attributeUpdates.put("Limit", AttributeValueUpdate.builder().value(AttributeValue.builder().s("limit").build()).build());
        attributeUpdates.put("TotalData", AttributeValueUpdate.builder().value(AttributeValue.builder().s("data").build()).build());
        attributeUpdates.put("Image", AttributeValueUpdate.builder().value(AttributeValue.builder().s("image").build()).build());
        attributeUpdates.put("Description", AttributeValueUpdate.builder().value(AttributeValue.builder().s("description").build()).build());
        assertEquals(attributeUpdates, request.attributeUpdates());
    }

    @Test
    void deleteCouponQuery() {
        QueryCouponsHelper helper = new QueryCouponsHelper();

        DeleteItemRequest request = helper.deleteCouponQuery("123", "type");

        assertEquals("coupon-table", request.tableName());

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("CouponType", AttributeValue.builder().s("type").build());
        key.put("CouponID", AttributeValue.builder().s("123").build());
        assertEquals(key, request.key());
    }

    @Test
    void getCouponDataByCouponId() {
        QueryCouponsHelper helper = new QueryCouponsHelper();
        GetItemRequest request = helper.getCouponDataByCouponId("123", "type");

        assertEquals("coupon-table", request.tableName());

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("CouponID", AttributeValue.builder().s("123").build());
        key.put("CouponType", AttributeValue.builder().s("type").build());
        assertEquals(key, request.key());
    }

    @Test
    void getAllCouponsByType() {
        QueryCouponsHelper helper = new QueryCouponsHelper();
        QueryRequest request = helper.getAllCouponsByType("type");

        assertEquals("coupon-table", request.tableName());

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":CouponType", AttributeValue.builder().s("type").build());
        assertEquals(expressionAttributeValues, request.expressionAttributeValues());

        assertEquals("CouponType = :CouponType", request.keyConditionExpression());
    }



    @Test
    void getAllCouponsByType_ValidType() {
        QueryCouponsHelper helper = new QueryCouponsHelper();
        QueryRequest request = helper.getAllCouponsByType("type");

        assertEquals("coupon-table", request.tableName());

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":CouponType", AttributeValue.builder().s("type").build());
        assertEquals(expressionAttributeValues, request.expressionAttributeValues());

        assertEquals("CouponType = :CouponType", request.keyConditionExpression());
    }

//    @Test
//    void getAllCouponsByType_NullType() {
//        QueryCouponsHelper helper = new QueryCouponsHelper();
//        assertThrows(NullPointerException.class, () -> helper.getAllCouponsByType(null));
//    }

    @Test
    void getAllCouponsByType_EmptyType() {
        QueryCouponsHelper helper = new QueryCouponsHelper();
        QueryRequest request = helper.getAllCouponsByType("");

        assertEquals("coupon-table", request.tableName());

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":CouponType", AttributeValue.builder().s("").build());
        assertEquals(expressionAttributeValues, request.expressionAttributeValues());

        assertEquals("CouponType = :CouponType", request.keyConditionExpression());
    }

    @Test
    void getAllCouponsByType_WhitespaceType() {
        QueryCouponsHelper helper = new QueryCouponsHelper();
        QueryRequest request = helper.getAllCouponsByType(" ");

        assertEquals("coupon-table", request.tableName());

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":CouponType", AttributeValue.builder().s(" ").build());
        assertEquals(expressionAttributeValues, request.expressionAttributeValues());

        assertEquals("CouponType = :CouponType", request.keyConditionExpression());
    }



}
