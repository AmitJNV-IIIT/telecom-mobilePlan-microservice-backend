package com.excitel.dynamoDbQueryHelp;

import com.excitel.dynamodbqueryhelp.QueryWrapper;
import com.excitel.model.Coupons;
import com.excitel.model.CustomerCoupon;
import com.excitel.model.MobilePlan;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class QueryWrapperTest {
    private static final String COUPON_ID = "CouponID";
    private static final String TOTAL_DATA = "TotalData";
    @Test
    void mapTomobilePlan() {
        QueryWrapper queryWrapper = new QueryWrapper();
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PlanType", AttributeValue.builder().s("Broadband").build());
        item.put("PlanID", AttributeValue.builder().s("123").build());
        item.put("Price", AttributeValue.builder().s("20").build());
        item.put("Category", AttributeValue.builder().s("Category").build());
        item.put("Validity", AttributeValue.builder().s("30 days").build());
        item.put("OTT", AttributeValue.builder().l(AttributeValue.builder().s("OTT1").build(), AttributeValue.builder().s("OTT2").build()).build());
        item.put("VoiceLimit", AttributeValue.builder().s("1000 mins").build());
        item.put("SMS", AttributeValue.builder().s("1000").build());
        item.put("TotalData", AttributeValue.builder().s("1 GB").build());
        item.put("CouponIDs", AttributeValue.builder().l(AttributeValue.builder().s("coupon1").build(), AttributeValue.builder().s("coupon2").build()).build());
        item.put("PlanLimit", AttributeValue.builder().s("Unlimited").build());
        item.put("Speed", AttributeValue.builder().s("100 Mbps").build());

        MobilePlan mobilePlan = queryWrapper.mapTomobilePlan(item);

        assertEquals("Broadband", mobilePlan.getPlanType());
        assertEquals("123", mobilePlan.getPlanId());
        assertEquals("20", mobilePlan.getPrice());
        assertEquals("Category", mobilePlan.getCategory());
        assertEquals("30 days", mobilePlan.getValidity());
        assertEquals(List.of("OTT1", "OTT2"), mobilePlan.getOtt());
        assertEquals("1000 mins", mobilePlan.getVoiceLimit());
        assertEquals("1000", mobilePlan.getSms());
        assertEquals("1 GB", mobilePlan.getData());
        assertEquals(List.of("coupon1", "coupon2"), mobilePlan.getCouponIds());
        assertEquals("Unlimited", mobilePlan.getLimit());
        assertEquals("100 Mbps", mobilePlan.getSpeed());
    }

    @Test
    void mapTomobilePlan_NullValues() {
        QueryWrapper queryWrapper = new QueryWrapper();
        Map<String, AttributeValue> item = new HashMap<>();

        MobilePlan mobilePlan = queryWrapper.mapTomobilePlan(item);

        assertNull(mobilePlan.getPlanType());
        assertNull(mobilePlan.getPlanId());
        assertNull(mobilePlan.getActive());
        assertNull(mobilePlan.getPrice());
        assertNull(mobilePlan.getCategory());
        assertNull(mobilePlan.getValidity());
        assertNull(mobilePlan.getOtt());
        assertNull(mobilePlan.getVoiceLimit());
        assertNull(mobilePlan.getSms());
        assertNull(mobilePlan.getData());
        assertNull(mobilePlan.getCouponIds());
        assertNull(mobilePlan.getLimit());
        assertNull(mobilePlan.getSpeed());
    }

    @Test
    void mapTomobilePlan_activeMissing() {
        QueryWrapper queryWrapper = new QueryWrapper();
        Map<String, AttributeValue> item = new HashMap<>();
        // We put all keys here except "Active"
        item.put("PlanType", AttributeValue.builder().s("Broadband").build());
        item.put("PlanID", AttributeValue.builder().s("123").build());
        item.put("Price", AttributeValue.builder().s("20").build());
        item.put("Category", AttributeValue.builder().s("Category").build());
        item.put("Validity", AttributeValue.builder().s("30 days").build());
        item.put("OTT", AttributeValue.builder().l(AttributeValue.builder().s("OTT1").build(), AttributeValue.builder().s("OTT2").build()).build());
        item.put("VoiceLimit", AttributeValue.builder().s("1000 mins").build());
        item.put("SMS", AttributeValue.builder().s("1000").build());
        item.put("TotalData", AttributeValue.builder().s("1 GB").build());
        item.put("CouponIDs", AttributeValue.builder().l(AttributeValue.builder().s("coupon1").build(), AttributeValue.builder().s("coupon2").build()).build());
        item.put("PlanLimit", AttributeValue.builder().s("Unlimited").build());
        item.put("Speed", AttributeValue.builder().s("100 Mbps").build());

        MobilePlan mobilePlan = queryWrapper.mapTomobilePlan(item);

        assertEquals("Broadband", mobilePlan.getPlanType());
        assertEquals("123", mobilePlan.getPlanId());
        assertNull(mobilePlan.getActive());  // Check that "Active" property is null
        assertEquals("20", mobilePlan.getPrice());
        assertEquals("Category", mobilePlan.getCategory());
        assertEquals("30 days", mobilePlan.getValidity());
        assertEquals(List.of("OTT1", "OTT2"), mobilePlan.getOtt());
        assertEquals("1000 mins", mobilePlan.getVoiceLimit());
        assertEquals("1000", mobilePlan.getSms());
        assertEquals("1 GB", mobilePlan.getData());
        assertEquals(List.of("coupon1", "coupon2"), mobilePlan.getCouponIds());
        assertEquals("Unlimited", mobilePlan.getLimit());
        assertEquals("100 Mbps", mobilePlan.getSpeed());
    }
    @Test
    void mapToCoupons() {
        QueryWrapper queryWrapper = new QueryWrapper();
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("CouponID", AttributeValue.builder().s("coupon123").build());
        item.put("CouponType", AttributeValue.builder().s("Discount").build());
        item.put("CouponCode", AttributeValue.builder().s("CODE123").build());
        item.put("Expire", AttributeValue.builder().s("2024-12-31").build());
        item.put("Image", AttributeValue.builder().s("image_url").build());
        item.put("TotalData", AttributeValue.builder().s("1 GB").build());
        item.put("Limit", AttributeValue.builder().s("10").build());

        Coupons coupons = queryWrapper.mapToCoupons(item);

        assertEquals("coupon123", coupons.getCouponId());
        assertEquals("Discount", coupons.getType());
        assertEquals("CODE123", coupons.getCouponCode());
        assertEquals("2024-12-31", coupons.getExpire());
        assertEquals("image_url", coupons.getImage());
        assertEquals("1 GB", coupons.getData());
        assertEquals("10", coupons.getLimit());
    }

    @Test
    void mapToCoupons_keysMissing() {
        QueryWrapper queryWrapper = new QueryWrapper();
        Map<String, AttributeValue> item = new HashMap<>();
        // We only put two keys here, the rest are missing
        item.put("CouponID", AttributeValue.builder().s("coupon123").build());
        item.put("CouponType", AttributeValue.builder().s("Discount").build());

        Coupons coupons = queryWrapper.mapToCoupons(item);

        assertEquals("coupon123", coupons.getCouponId());
        assertEquals("Discount", coupons.getType());
        // For the missing keys, we check that the corresponding properties are set to their default values
        assertNull(coupons.getCouponCode());
        assertNull(coupons.getExpire());
        assertNull(coupons.getImage());
        assertNull(coupons.getData());
        assertNull(coupons.getLimit());
        assertEquals("Not defined", coupons.getDescription());
    }

    @Test
    void mapToCustomerCoupon() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<Map<String, AttributeValue>> items = new ArrayList<>();
        Map<String, AttributeValue> item1 = new HashMap<>();
        item1.put("MobileNumber", AttributeValue.builder().s("1234567890").build());
        item1.put("CouponID", AttributeValue.builder().s("coupon1").build());
        item1.put("CouponStatus", AttributeValue.builder().s("ACTIVE").build());
        item1.put("ActivationDate", AttributeValue.builder().s("2024-04-28").build());
        item1.put("CustomerCouponID", AttributeValue.builder().s("custCoupon1").build());
        Map<String, AttributeValue> item2 = new HashMap<>();
        item2.put("MobileNumber", AttributeValue.builder().s("9876543210").build());
        item2.put("CouponID", AttributeValue.builder().s("coupon2").build());
        item2.put("CouponStatus", AttributeValue.builder().s("INACTIVE").build());
        item2.put("ActivationDate", AttributeValue.builder().s("2024-04-29").build());
        item2.put("CustomerCouponID", AttributeValue.builder().s("custCoupon2").build());
        items.add(item1);
        items.add(item2);

        List<CustomerCoupon> customerCoupons = queryWrapper.mapToCustomerCoupon(items);

        assertEquals(2, customerCoupons.size());
        assertEquals("1234567890", customerCoupons.get(0).getMobileNumber());
        assertEquals("coupon1", customerCoupons.get(0).getCouponID());
        assertEquals("ACTIVE", customerCoupons.get(0).getCouponStatus());
        assertEquals("2024-04-28", customerCoupons.get(0).getActivationDate());
        assertEquals("custCoupon1", customerCoupons.get(0).getCustomerCouponId());
        assertEquals("9876543210", customerCoupons.get(1).getMobileNumber());
        assertEquals("coupon2", customerCoupons.get(1).getCouponID());
        assertEquals("INACTIVE", customerCoupons.get(1).getCouponStatus());
        assertEquals("2024-04-29", customerCoupons.get(1).getActivationDate());
        assertEquals("custCoupon2", customerCoupons.get(1).getCustomerCouponId());
    }

    @Test
    void mapToCustomerCoupon_EmptyList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<Map<String, AttributeValue>> items = new ArrayList<>();

        List<CustomerCoupon> customerCoupons = queryWrapper.mapToCustomerCoupon(items);

        assertEquals(0, customerCoupons.size());
    }

    @Test
    void mapToCustomerCoupon_keysMissing() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<Map<String, AttributeValue>> items = new ArrayList<>();
        Map<String, AttributeValue> item = new HashMap<>();
        // We only put two keys here, the rest are missing
        item.put("MobileNumber", AttributeValue.builder().s("1234567890").build());
        item.put("CouponID", AttributeValue.builder().s("coupon1").build());
        items.add(item);

        List<CustomerCoupon> customerCoupons = queryWrapper.mapToCustomerCoupon(items);

        assertEquals(1, customerCoupons.size());
        assertEquals("1234567890", customerCoupons.get(0).getMobileNumber());
        assertEquals("coupon1", customerCoupons.get(0).getCouponID());
        // For the missing keys, we check that the corresponding properties are set to their default values
        assertNull(customerCoupons.get(0).getCouponStatus());
        assertNull(customerCoupons.get(0).getActivationDate());
        assertNull(customerCoupons.get(0).getCustomerCouponId());
    }
    @Test
    void mapToCouponTotalData_WithValue() {
        QueryWrapper queryWrapper = new QueryWrapper();
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("TotalData", AttributeValue.builder().s("1 GB").build());

        String totalData = queryWrapper.mapToCouponTotalData(item);

        assertEquals("1 GB", totalData);
    }

    @Test
    void mapToCouponTotalData_WithoutValue() {
        QueryWrapper queryWrapper = new QueryWrapper();
        Map<String, AttributeValue> item = new HashMap<>();

        String totalData = queryWrapper.mapToCouponTotalData(item);

        assertNull(totalData);
    }

    @Test
    void mapToCouponsForUser_AllAttributesPresent() {
        QueryWrapper queryWrapper = new QueryWrapper();
        // Arrange
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(COUPON_ID, AttributeValue.builder().s("couponIdValue").build());
        item.put("CouponType", AttributeValue.builder().s("couponTypeValue").build());
        item.put("Expire", AttributeValue.builder().s("expireValue").build());
        item.put("Image", AttributeValue.builder().s("imageUrl").build());
        item.put(TOTAL_DATA, AttributeValue.builder().s("dataValue").build());
        item.put("Description", AttributeValue.builder().s("descriptionValue").build());

        // Act
        Coupons result = queryWrapper.mapToCouponsForUser(item);;

        // Assert
        assertEquals("couponIdValue", result.getCouponId());
        assertEquals("couponTypeValue", result.getType());
        assertEquals("expireValue", result.getExpire());
        assertEquals("imageUrl", result.getImage());
        assertEquals("dataValue", result.getData());
        assertEquals("descriptionValue", result.getDescription());
    }
    @Test
    void mapToCouponsForUser_SomeAttributesMissing() {
        QueryWrapper queryWrapper = new QueryWrapper();
        // Arrange
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(COUPON_ID, AttributeValue.builder().s("couponIdValue").build());
        // CouponType and Expire attributes are missing

        // Act
        Coupons result = queryWrapper.mapToCouponsForUser(item);

        // Assert
        assertEquals("couponIdValue", result.getCouponId());
        assertNull(result.getType());
        assertNull(result.getExpire());
        assertNull(result.getImage());
        assertNull(result.getData());
        assertEquals("Not defined", result.getDescription());
    }
    @Test
    void mapToCouponsForUser_AllAttributesMissing() {
        QueryWrapper queryWrapper = new QueryWrapper();
        // Arrange
        Map<String, AttributeValue> item = new HashMap<>();
        // All attributes are missing

        // Act
        Coupons result = queryWrapper.mapToCouponsForUser(item);

        // Assert
        assertNull(result.getCouponId());
        assertNull(result.getType());
        assertNull(result.getExpire());
        assertNull(result.getImage());
        assertNull(result.getData());
        assertEquals("Not defined", result.getDescription());
    }
}
