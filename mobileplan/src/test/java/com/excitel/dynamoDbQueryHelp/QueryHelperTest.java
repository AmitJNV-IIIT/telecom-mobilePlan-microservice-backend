package com.excitel.dynamoDbQueryHelp;

import com.excitel.dynamodbqueryhelp.QueryHelper;
import com.excitel.model.MobilePlan;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class QueryHelperTest {

    @Test
    void getItemByPlanId() {
        QueryHelper helper = new QueryHelper();
        GetItemRequest request = helper.getItemByPlanId("123", "Broadband");

        assertEquals("plan-table", request.tableName());
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("PlanType", AttributeValue.builder().s("Broadband").build());
        key.put("PlanID", AttributeValue.builder().s("123").build());
        assertEquals(key, request.key());
    }

    @Test
    void addQuery() {
        QueryHelper helper = new QueryHelper();
        MobilePlan mobilePlan = new MobilePlan();
        mobilePlan.setPlanId("123");
        mobilePlan.setPlanType("Broadband");
        mobilePlan.setPrice("10");
        mobilePlan.setCategory("Category");
        mobilePlan.setValidity("30 days");
        mobilePlan.setOtt(Arrays.asList("OTT1", "OTT2"));
        mobilePlan.setVoiceLimit("1000 mins");
        mobilePlan.setSms("1000");
        mobilePlan.setData("1 GB");
        mobilePlan.setCouponIds(Arrays.asList("coupon1", "coupon2"));
        mobilePlan.setLimit("Unlimited");
        mobilePlan.setActive("True");

        PutItemRequest request = helper.addQuery(mobilePlan);

        assertEquals("plan-table", request.tableName());
        assertEquals("attribute_not_exists(PlanType) AND attribute_not_exists(PlanID)", request.conditionExpression());

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PlanID", AttributeValue.builder().s("123").build());
        item.put("PlanType", AttributeValue.builder().s("Broadband").build());
        item.put("Price", AttributeValue.builder().s("10").build());
        item.put("Category", AttributeValue.builder().s("Category").build());
        item.put("Validity", AttributeValue.builder().s("30 days").build());
        item.put("OTT", AttributeValue.builder().l(
                AttributeValue.builder().s("OTT1").build(),
                AttributeValue.builder().s("OTT2").build()
        ).build());
        item.put("VoiceLimit", AttributeValue.builder().s("1000 mins").build());
        item.put("SMS", AttributeValue.builder().s("1000").build());
        item.put("TotalData", AttributeValue.builder().s("1 GB").build());
        item.put("CouponIDs", AttributeValue.builder().l(
                AttributeValue.builder().s("coupon1").build(),
                AttributeValue.builder().s("coupon2").build()
        ).build());
        item.put("PlanLimit", AttributeValue.builder().s("Unlimited").build());
        item.put("Active", AttributeValue.builder().s("True").build());

        assertEquals(item, request.item());
    }


    @Test
    void addQuery_empty_list() {
        QueryHelper helper = new QueryHelper();
        MobilePlan mobilePlan = new MobilePlan();
        mobilePlan.setPlanId("123");
        mobilePlan.setPlanType("Broadband");
        mobilePlan.setPrice("10");
        mobilePlan.setCategory("Category");
        mobilePlan.setValidity("30 days");
        mobilePlan.setOtt(Collections.emptyList());
        mobilePlan.setVoiceLimit("1000 mins");
        mobilePlan.setSms("1000");
        mobilePlan.setData("1 GB");
        mobilePlan.setCouponIds(Collections.emptyList());
        mobilePlan.setLimit("Unlimited");
        mobilePlan.setActive("True");

        PutItemRequest request = helper.addQuery(mobilePlan);

        assertEquals("plan-table", request.tableName());
        assertEquals("attribute_not_exists(PlanType) AND attribute_not_exists(PlanID)", request.conditionExpression());

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PlanID", AttributeValue.builder().s("123").build());
        item.put("PlanType", AttributeValue.builder().s("Broadband").build());
        item.put("Price", AttributeValue.builder().s("10").build());
        item.put("Category", AttributeValue.builder().s("Category").build());
        item.put("Validity", AttributeValue.builder().s("30 days").build());
        item.put("OTT", AttributeValue.builder().l(Collections.emptyList()).build());
        item.put("VoiceLimit", AttributeValue.builder().s("1000 mins").build());
        item.put("SMS", AttributeValue.builder().s("1000").build());
        item.put("TotalData", AttributeValue.builder().s("1 GB").build());
        item.put("CouponIDs", AttributeValue.builder().l(Collections.emptyList()).build());
        item.put("PlanLimit", AttributeValue.builder().s("Unlimited").build());
        item.put("Active", AttributeValue.builder().s("True").build());

        assertEquals(item, request.item());
    }
    @Test
    void addQuery_null_fields() {
        QueryHelper helper = new QueryHelper();
        MobilePlan mobilePlan = new MobilePlan();
        mobilePlan.setPlanId("123");
        mobilePlan.setPlanType(null);
        mobilePlan.setPrice(null);
        mobilePlan.setCategory(null);
        mobilePlan.setValidity(null);
        mobilePlan.setVoiceLimit(null);
        mobilePlan.setSms(null);
        mobilePlan.setData(null);
        mobilePlan.setLimit(null);
        mobilePlan.setActive(null);
        mobilePlan.setOtt(null);
        mobilePlan.setCouponIds(null);

        PutItemRequest request = helper.addQuery(mobilePlan);

        assertEquals("plan-table", request.tableName());
        assertEquals("attribute_not_exists(PlanType) AND attribute_not_exists(PlanID)", request.conditionExpression());

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PlanID", AttributeValue.builder().s("123").build());
        item.put("PlanType", AttributeValue.builder().s("Broadband").build());
        item.put("Price", AttributeValue.builder().s("null").build());
        item.put("Category", AttributeValue.builder().s("null").build());
        item.put("Validity", AttributeValue.builder().s("null").build());
        item.put("VoiceLimit", AttributeValue.builder().s("null").build());
        item.put("SMS", AttributeValue.builder().s("null").build());
        item.put("TotalData", AttributeValue.builder().s("null").build());
        item.put("PlanLimit", AttributeValue.builder().s("null").build());
        item.put("Active", AttributeValue.builder().s("True").build());
        item.put("OTT", AttributeValue.builder().l(Collections.emptyList()).build());
        item.put("CouponIDs", AttributeValue.builder().l(Collections.emptyList()).build());

        assertEquals(item, request.item());
    }
    @Test
    void updateQuery() {
        QueryHelper helper = new QueryHelper();
        MobilePlan mobilePlan = new MobilePlan();
        mobilePlan.setPlanId("123");
        mobilePlan.setPlanType("Broadband");
        mobilePlan.setPrice("20");
        mobilePlan.setCategory("New Category");
        mobilePlan.setValidity("60 days");
        mobilePlan.setOtt(Arrays.asList("OTT3", "OTT4"));
        mobilePlan.setVoiceLimit("2000 mins");
        mobilePlan.setSms("2000");
        mobilePlan.setData("2 GB");
        mobilePlan.setCouponIds(Arrays.asList("coupon3", "coupon4"));
        mobilePlan.setLimit("Limited");
        mobilePlan.setActive("False");

        UpdateItemRequest request = helper.updateQuery(mobilePlan, "123");

        assertEquals("plan-table", request.tableName());

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("PlanType", AttributeValue.builder().s("Broadband").build());
        key.put("PlanID", AttributeValue.builder().s("123").build());
        assertEquals(key, request.key());

        Map<String, AttributeValueUpdate> attributeUpdates = new HashMap<>();
        attributeUpdates.put("Price", AttributeValueUpdate.builder().value(AttributeValue.builder().s("20").build()).build());
        attributeUpdates.put("Category", AttributeValueUpdate.builder().value(AttributeValue.builder().s("New Category").build()).build());
        attributeUpdates.put("Validity", AttributeValueUpdate.builder().value(AttributeValue.builder().s("60 days").build()).build());
        attributeUpdates.put("OTT", AttributeValueUpdate.builder().value(AttributeValue.builder().l(
                AttributeValue.builder().s("OTT3").build(),
                AttributeValue.builder().s("OTT4").build()
        ).build()).build());
        attributeUpdates.put("VoiceLimit", AttributeValueUpdate.builder().value(AttributeValue.builder().s("2000 mins").build()).build());
        attributeUpdates.put("SMS", AttributeValueUpdate.builder().value(AttributeValue.builder().s("2000").build()).build());
        attributeUpdates.put("TotalData", AttributeValueUpdate.builder().value(AttributeValue.builder().s("2 GB").build()).build());
        attributeUpdates.put("CouponIDs", AttributeValueUpdate.builder().value(AttributeValue.builder().l(
                AttributeValue.builder().s("coupon3").build(),
                AttributeValue.builder().s("coupon4").build()
        ).build()).build());
        attributeUpdates.put("PlanLimit", AttributeValueUpdate.builder().value(AttributeValue.builder().s("Limited").build()).build());
        attributeUpdates.put("Active", AttributeValueUpdate.builder().value(AttributeValue.builder().s("False").build()).build());

        assertEquals(attributeUpdates, request.attributeUpdates());
    }

    @Test
    void updateQuery_null_list_fields() {
        QueryHelper helper = new QueryHelper();
        MobilePlan mobilePlan = new MobilePlan();
        mobilePlan.setPlanId("123");
        mobilePlan.setPlanType("Broadband");
        mobilePlan.setPrice("20");
        mobilePlan.setCategory("New Category");
        mobilePlan.setValidity("60 days");
        mobilePlan.setOtt(null);
        mobilePlan.setVoiceLimit("2000 mins");
        mobilePlan.setSms("2000");
        mobilePlan.setData("2 GB");
        mobilePlan.setCouponIds(null);
        mobilePlan.setLimit("Limited");
        mobilePlan.setActive("False");

        UpdateItemRequest request = helper.updateQuery(mobilePlan, "123");

        assertEquals("plan-table", request.tableName());

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("PlanType", AttributeValue.builder().s("Broadband").build());
        key.put("PlanID", AttributeValue.builder().s("123").build());
        assertEquals(key, request.key());

        Map<String, AttributeValueUpdate> attributeUpdates = new HashMap<>();
        attributeUpdates.put("Price", AttributeValueUpdate.builder().value(AttributeValue.builder().s("20").build()).build());
        attributeUpdates.put("Category", AttributeValueUpdate.builder().value(AttributeValue.builder().s("New Category").build()).build());
        attributeUpdates.put("Validity", AttributeValueUpdate.builder().value(AttributeValue.builder().s("60 days").build()).build());
        attributeUpdates.put("OTT", AttributeValueUpdate.builder().value(AttributeValue.builder().l(Collections.emptyList()).build()).build());
        attributeUpdates.put("VoiceLimit", AttributeValueUpdate.builder().value(AttributeValue.builder().s("2000 mins").build()).build());
        attributeUpdates.put("SMS", AttributeValueUpdate.builder().value(AttributeValue.builder().s("2000").build()).build());
        attributeUpdates.put("TotalData", AttributeValueUpdate.builder().value(AttributeValue.builder().s("2 GB").build()).build());
        attributeUpdates.put("CouponIDs", AttributeValueUpdate.builder().value(AttributeValue.builder().l(Collections.emptyList()).build()).build());
        attributeUpdates.put("PlanLimit", AttributeValueUpdate.builder().value(AttributeValue.builder().s("Limited").build()).build());
        attributeUpdates.put("Active", AttributeValueUpdate.builder().value(AttributeValue.builder().s("False").build()).build());

        assertEquals(attributeUpdates, request.attributeUpdates());
    }

    @Test
    void deleteQuery() {
        QueryHelper helper = new QueryHelper();

        UpdateItemRequest request = helper.deleteQuery("123", new MobilePlan());

        assertEquals("plan-table", request.tableName());

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("PlanType", AttributeValue.builder().nul(true).build()); // Corrected to represent null value
        key.put("PlanID", AttributeValue.builder().build());
//        Assertions.assertNotEquals(key, request.key());

        Map<String, AttributeValueUpdate> attributeUpdates = new HashMap<>();
        attributeUpdates.put("Active", AttributeValueUpdate.builder().value(AttributeValue.builder().s("False").build()).build());

        assertEquals(attributeUpdates, request.attributeUpdates());
    }
}
