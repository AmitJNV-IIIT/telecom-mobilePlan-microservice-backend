package com.excitel.serviceimpl.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.excitel.dto.CouponResponseDTO;
import com.excitel.dto.RequestDTO;
import com.excitel.dynamodbqueryhelp.QueryCouponsHelper;
import com.excitel.dynamodbqueryhelp.QueryWrapper;
import com.excitel.exception.custom.DatabaseConnectionException;
import com.excitel.exception.custom.ResourceNotFoundException;
import com.excitel.model.Coupons;
import com.excitel.model.MobilePlan;
import com.excitel.redishelper.MobilePlanRedis;
import com.excitel.service.admin.AdminService;
import com.excitel.serviceimpl.admin.AdminServiceImpl;

import java.util.*;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@ContextConfiguration(classes = {MobilePlanServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class MobilePlanServiceImplTest {
    @MockBean
    private AdminServiceImpl adminService;

    @MockBean
    private AmazonDynamoDB amazonDynamoDB;

    @MockBean
    private DynamoDbClient dynamoDbClient;

    @MockBean
    private MobilePlanRedis mobilePlanRedis;

    @Autowired
    private MobilePlanServiceImpl mobilePlanServiceImpl;

    @MockBean
    private QueryCouponsHelper queryCouponsHelper;

    @MockBean
    private QueryWrapper queryWrapper;

    @Mock
    private QueryResponse queryResponse;

    @Mock
    Coupons coupon;


    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#getMobilePlanWithQuery(RequestDTO)}
     */
    @Test
    void testGetMobilePlanWithQuery() {
        // Arrange
        when(mobilePlanRedis.generateKeyFromParams(Mockito.<RequestDTO>any()))
                .thenThrow(new DatabaseConnectionException("An error occurred"));

        // Act and Assert
        assertThrows(DatabaseConnectionException.class,
                () -> mobilePlanServiceImpl.getMobilePlanWithQuery(new RequestDTO()));
        verify(mobilePlanRedis).generateKeyFromParams(isA(RequestDTO.class));
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#getMobilePlanWithQuery(RequestDTO)}
     */
    @Test
    void testGetMobilePlanWithQuery2() {
        // Arrange
        ArrayList<MobilePlan> mobilePlanList = new ArrayList<>();
        MobilePlan.MobilePlanBuilder categoryResult = MobilePlan.builder().active("Active").category("Category");
        MobilePlan.MobilePlanBuilder limitResult = categoryResult.couponIds(new ArrayList<>()).data("Data").limit("Limit");
        MobilePlan buildResult = limitResult.ott(new ArrayList<>())
                .planId("42")
                .planType("Plan Type")
                .price("Price")
                .sms("Sms")
                .speed("Speed")
                .validity("Validity")
                .voiceLimit("Voice Limit")
                .build();
        mobilePlanList.add(buildResult);
        when(mobilePlanRedis.generateKeyFromParams(Mockito.<RequestDTO>any())).thenReturn("jane.doe@example.org");
        when(mobilePlanRedis.getMobilePlansCache(Mockito.<String>any())).thenReturn(mobilePlanList);

        // Act
        List<MobilePlan> actualMobilePlanWithQuery = mobilePlanServiceImpl.getMobilePlanWithQuery(new RequestDTO());

        // Assert
        verify(mobilePlanRedis).generateKeyFromParams(isA(RequestDTO.class));
        verify(mobilePlanRedis).getMobilePlansCache("jane.doe@example.org");
        assertEquals(1, actualMobilePlanWithQuery.size());
        assertSame(mobilePlanList, actualMobilePlanWithQuery);
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#buildQueryRequest(String, String, String, String, String, String)}
     */
    @Test
    void testBuildQueryRequest() {
        // Arrange and Act
        QueryRequest actualBuildQueryRequestResult = mobilePlanServiceImpl.buildQueryRequest("Type", "Active", "42",
                "Category", "Data", "Days");

        // Assert
        Map<String, Condition> expectedKeyConditionsResult = actualBuildQueryRequestResult.queryFilter();
        assertSame(expectedKeyConditionsResult, actualBuildQueryRequestResult.keyConditions());
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#buildQueryRequest(String, String, String, String, String, String)}
     */
    @Test
    void testBuildQueryRequest2() {
        // Arrange and Act
        QueryRequest actualBuildQueryRequestResult = mobilePlanServiceImpl.buildQueryRequest("plan-table", "Active", "42",
                "Category", "Data", "Days");

        // Assert
        Map<String, Condition> expectedKeyConditionsResult = actualBuildQueryRequestResult.queryFilter();
        assertSame(expectedKeyConditionsResult, actualBuildQueryRequestResult.keyConditions());
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#buildQueryRequest(String, String, String, String, String, String)}
     */
    @Test
    void testBuildQueryRequest3() {
        // Arrange and Act
        QueryRequest actualBuildQueryRequestResult = mobilePlanServiceImpl.buildQueryRequest("Type", null, "42", "Category",
                "Data", "Days");

        // Assert
        Map<String, Condition> expectedKeyConditionsResult = actualBuildQueryRequestResult.queryFilter();
        assertSame(expectedKeyConditionsResult, actualBuildQueryRequestResult.keyConditions());
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#buildQueryRequest(String, String, String, String, String, String)}
     */
    @Test
    void testBuildQueryRequest4() {
        // Arrange and Act
        QueryRequest actualBuildQueryRequestResult = mobilePlanServiceImpl.buildQueryRequest("Type", "Active", null,
                "Category", "Data", "Days");

        // Assert
        Map<String, Condition> expectedKeyConditionsResult = actualBuildQueryRequestResult.queryFilter();
        assertSame(expectedKeyConditionsResult, actualBuildQueryRequestResult.keyConditions());
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#buildQueryRequest(String, String, String, String, String, String)}
     */
    @Test
    void testBuildQueryRequest5() {
        // Arrange and Act
        QueryRequest actualBuildQueryRequestResult = mobilePlanServiceImpl.buildQueryRequest("Type", null, null, "Category",
                "Data", "Days");

        // Assert
        Map<String, Condition> expectedKeyConditionsResult = actualBuildQueryRequestResult.queryFilter();
        assertSame(expectedKeyConditionsResult, actualBuildQueryRequestResult.keyConditions());
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#buildQueryRequest(String, String, String, String, String, String)}
     */
    @Test
    void testBuildQueryRequest6() {
        // Arrange and Act
        QueryRequest actualBuildQueryRequestResult = mobilePlanServiceImpl.buildQueryRequest(null, "Active", null,
                "Category", "Data", "Days");

        // Assert
        Map<String, Condition> expectedKeyConditionsResult = actualBuildQueryRequestResult.queryFilter();
        assertSame(expectedKeyConditionsResult, actualBuildQueryRequestResult.keyConditions());
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#buildQueryRequest(String, String, String, String, String, String)}
     */
    @Test
    void testBuildQueryRequest7() {
        // Arrange and Act
        QueryRequest actualBuildQueryRequestResult = mobilePlanServiceImpl.buildQueryRequest("Type", "Active", null, null,
                "Data", "Days");

        // Assert
        Map<String, Condition> expectedKeyConditionsResult = actualBuildQueryRequestResult.queryFilter();
        assertSame(expectedKeyConditionsResult, actualBuildQueryRequestResult.keyConditions());
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#buildQueryRequest(String, String, String, String, String, String)}
     */
    @Test
    void testBuildQueryRequest8() {
        // Arrange and Act
        QueryRequest actualBuildQueryRequestResult = mobilePlanServiceImpl.buildQueryRequest("Type", "Active", null,
                "Category", null, "Days");

        // Assert
        Map<String, Condition> expectedKeyConditionsResult = actualBuildQueryRequestResult.queryFilter();
        assertSame(expectedKeyConditionsResult, actualBuildQueryRequestResult.keyConditions());
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#buildQueryRequest(String, String, String, String, String, String)}
     */
    @Test
    void testBuildQueryRequest9() {
        // Arrange and Act
        QueryRequest actualBuildQueryRequestResult = mobilePlanServiceImpl.buildQueryRequest("Type", "Active", null,
                "Category", "Data", null);

        // Assert
        Map<String, Condition> expectedKeyConditionsResult = actualBuildQueryRequestResult.queryFilter();
        assertSame(expectedKeyConditionsResult, actualBuildQueryRequestResult.keyConditions());
    }

    @Test
    void testGetMobilePlanWithQuery_CacheIsNull() {
        // Given
        RequestDTO params = new RequestDTO();
        params.setActive("active");
        params.setPlanId("planId");
        when(mobilePlanRedis.getMobilePlansCache(anyString())).thenReturn(null);
        when(dynamoDbClient.query((QueryRequest) Mockito.any())).thenReturn(queryResponse);

        // When
        List<MobilePlan> result = mobilePlanServiceImpl.getMobilePlanWithQuery(params);

        // Then
        Assertions.assertNotNull(result);
    }

    @Test
    void testGetMobilePlanWithQuery_CacheIsEmpty() {
        // Given
        RequestDTO params = new RequestDTO();
        params.setActive("active");
        params.setPlanId("planId");
        when(mobilePlanRedis.getMobilePlansCache(anyString())).thenReturn(new ArrayList<>());
        when(dynamoDbClient.query((QueryRequest) Mockito.any())).thenReturn(queryResponse);

        // When
        List<MobilePlan> result = mobilePlanServiceImpl.getMobilePlanWithQuery(params);

        // Then
        Assertions.assertNotNull(result);
    }

    @Test
    void testBuildQueryRequest_WithoutFilterConditions() {
        // Given
        String type = "type";
        String active = null;
        String planId = null;
        String category = null;
        String data = null;
        String days = null;

        Map<String, AttributeValue> expectedAttributeValues = new HashMap<>();
        expectedAttributeValues.put(":type", AttributeValue.builder().s(type).build());

        // When
        QueryRequest result = mobilePlanServiceImpl.buildQueryRequest(type, active, planId, category, data, days);

        // Then
        Assertions.assertEquals(expectedAttributeValues, result.expressionAttributeValues());
    }


    /**
     * Method under test: {@link MobilePlanServiceImpl#getByCouponId(String)}
     */
    @Test
    void testGetByCouponId() {
        // Arrange
        when(queryCouponsHelper.getItemByCouponId(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> mobilePlanServiceImpl.getByCouponId("42"));
        verify(queryCouponsHelper).getItemByCouponId("Internal", "42");
    }

    @Test
    void testGetByCouponId_try() {
        // Given
        String couponId = "couponId";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("key", AttributeValue.builder().s("value").build());

        when(dynamoDbClient.getItem((GetItemRequest) any())).thenReturn(GetItemResponse.builder().item(item).build());
        Coupons expectedCoupon = new Coupons();
        when(queryWrapper.mapToCoupons(item)).thenReturn(expectedCoupon);

        // When
        Coupons result = mobilePlanServiceImpl.getByCouponId(couponId);

        // Then
        Assertions.assertEquals(expectedCoupon, result);
    }


    @Test
    void testGetByCouponId_ThrowsExceptionWhenResponseIsEmpty() {
        // Given
        String couponId = "couponId";
        when(dynamoDbClient.getItem((GetItemRequest) any())).thenReturn(GetItemResponse.builder().item(Collections.emptyMap()).build());

        // When & Then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> mobilePlanServiceImpl.getByCouponId(couponId));
    }

    @Test
    void testGetMobilePlanWithQuery_MapItemsToMobilePlan() {
        // Given
        RequestDTO params = new RequestDTO();
        params.setActive("active");
        params.setPlanId("planId");
        when(mobilePlanRedis.getMobilePlansCache(anyString())).thenReturn(null);
        when(dynamoDbClient.query((QueryRequest) Mockito.any())).thenReturn(queryResponse);

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("key", AttributeValue.builder().s("value").build());
        List<Map<String, AttributeValue>> items = new ArrayList<>();
        items.add(item);
        when(queryResponse.items()).thenReturn(items);

        MobilePlan mobilePlan = new MobilePlan();
        when(queryWrapper.mapTomobilePlan(item)).thenReturn(mobilePlan);

        // When
        List<MobilePlan> result = mobilePlanServiceImpl.getMobilePlanWithQuery(params);

        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(mobilePlan, result.get(0));
    }

    @Test
    void testGetAllCouponByCouponIdList1() {
        // Given
        List<String> couponIds = new ArrayList<>();
        couponIds.add("couponId1");
        couponIds.add("couponId2");
        String couponType = "couponType";

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("key", AttributeValue.builder().s("value").build());
        List<Map<String, AttributeValue>> itemList = new ArrayList<>();
        itemList.add(item);
        Map<String, List<Map<String, AttributeValue>>> responseMap = new HashMap<>();
        responseMap.put("coupon-table", itemList);

        when(dynamoDbClient.batchGetItem((BatchGetItemRequest) any())).thenReturn(BatchGetItemResponse.builder().responses(responseMap).build());

        // When
        CouponResponseDTO result = mobilePlanServiceImpl.getAllCouponByCouponIdList(couponIds, couponType);

        // Then
        Assertions.assertNotNull(result);
    }

    /**
     * Method under test: {@link MobilePlanServiceImpl#getByCouponId(String)}
     */
    @Test
    void testGetByCouponId2() {
        // Arrange
        when(queryCouponsHelper.getItemByCouponId(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new DatabaseConnectionException("An error occurred"));

        // Act and Assert
        assertThrows(DatabaseConnectionException.class, () -> mobilePlanServiceImpl.getByCouponId("42"));
        verify(queryCouponsHelper).getItemByCouponId("Internal", "42");
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#getAllCouponByCouponIdList(List, String)}
     */
    @Test
    void testGetAllCouponByCouponIdList() throws AwsServiceException, SdkClientException {
        // Arrange
        when(dynamoDbClient.batchGetItem(Mockito.<BatchGetItemRequest>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> mobilePlanServiceImpl.getAllCouponByCouponIdList(new ArrayList<>(), "Coupon Type"));
        verify(dynamoDbClient).batchGetItem(isA(BatchGetItemRequest.class));
    }


    @Test
    void testGetCouponDataByCouponId1() {
        // Given
        String couponId = "couponId";
        String couponType = "couponType";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("key", AttributeValue.builder().s("value").build());

        when(dynamoDbClient.getItem((GetItemRequest) any())).thenReturn(GetItemResponse.builder().item(item).build());
        String expectedData = "expectedData";
        when(queryWrapper.mapToCouponTotalData(item)).thenReturn(expectedData);

        // When
        String result = mobilePlanServiceImpl.getCouponDataByCouponId(couponId, couponType);

        // Then
        Assertions.assertEquals(expectedData, result);
    }

    @Test
    void testGetCouponDataByCouponId_ThrowsExceptionWhenResponseIsEmpty() {
        // Given
        String couponId = "couponId";
        String couponType = "couponType";
        when(dynamoDbClient.getItem((GetItemRequest) any())).thenReturn(GetItemResponse.builder().item(Collections.emptyMap()).build());

        // When & Then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> mobilePlanServiceImpl.getCouponDataByCouponId(couponId, couponType));
    }

        /**
         * Method under test: {@link MobilePlanServiceImpl#mapToCouponDetail(Map)}
         */
    @Test
    void testMapToCouponDetail() {
        // Arrange and Act
        Coupons actualMapToCouponDetailResult = mobilePlanServiceImpl.mapToCouponDetail(new HashMap<>());

        // Assert
        assertNull(actualMapToCouponDetailResult.getCouponCode());
        assertNull(actualMapToCouponDetailResult.getCouponId());
        assertNull(actualMapToCouponDetailResult.getData());
        assertNull(actualMapToCouponDetailResult.getDescription());
        assertNull(actualMapToCouponDetailResult.getExpire());
        assertNull(actualMapToCouponDetailResult.getImage());
        assertNull(actualMapToCouponDetailResult.getLimit());
        assertNull(actualMapToCouponDetailResult.getType());
        assertNull(actualMapToCouponDetailResult.getUrl());
    }

    /**
     * Method under test: {@link MobilePlanServiceImpl#getStringOrNull(Map, String)}
     */
    @Test
    void testGetStringOrNull() {
        // Arrange, Act and Assert
        assertNull(mobilePlanServiceImpl.getStringOrNull(new HashMap<>(), "Key"));
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#getCouponDataByCouponId(String, String)}
     */
    @Test
    void testGetCouponDataByCouponId() throws AwsServiceException, SdkClientException {
        // Arrange
        when(dynamoDbClient.getItem(Mockito.<GetItemRequest>any())).thenReturn(null);
        when(queryCouponsHelper.getCouponDataByCouponId(Mockito.<String>any(), Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> mobilePlanServiceImpl.getCouponDataByCouponId("42", "Coupon Type"));
        verify(queryCouponsHelper).getCouponDataByCouponId("42", "Coupon Type");
        verify(dynamoDbClient).getItem((GetItemRequest) isNull());
    }

    /**
     * Method under test:
     * {@link MobilePlanServiceImpl#getCouponDataByCouponId(String, String)}
     */
    @Test
    void testGetCouponDataByCouponId2() {
        // Arrange
        when(queryCouponsHelper.getCouponDataByCouponId(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> mobilePlanServiceImpl.getCouponDataByCouponId("42", "Coupon Type"));
        verify(queryCouponsHelper).getCouponDataByCouponId("42", "Coupon Type");
    }

    /**
     * Method under test: {@link MobilePlanServiceImpl#updateLimit(String)}
     */
    @Test
    void testUpdateLimit() {
        // Arrange
        when(queryCouponsHelper.getItemByCouponId(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> mobilePlanServiceImpl.updateLimit("42"));
        verify(queryCouponsHelper).getItemByCouponId("Internal", "42");
    }

    /**
     * Method under test: {@link MobilePlanServiceImpl#updateLimit(String)}
     */
    @Test
    void testUpadteLimit2() {
        // Arrange
        when(queryCouponsHelper.getItemByCouponId(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new DatabaseConnectionException("An error occurred"));

        // Act and Assert
        assertThrows(DatabaseConnectionException.class, () -> mobilePlanServiceImpl.updateLimit("42"));
//        verify(queryCouponsHelper).getItemByCouponId("Internal", eq("42"));
    }

    @Test
    void shouldDeleteCouponIfLimitIsOne() {
        // Arrange
//        String couponId = "123";
//        Coupons coupon = new Coupons();
//        coupon.setLimit("1");
//
//        // Mocking to return specific coupon
//        when(adminService.getCouponById(couponId)).thenReturn(coupon);
//
//        // Act
//        Boolean result = mobilePlanServiceImpl.updateLimit(couponId);
//
//        // Assert
//        assertTrue(result);
//        verify(adminService).deleteCouponById(couponId);  // verify if deleteCouponById was called
    }

    @Test
    void shouldUpdateCouponIfLimitIsMoreThanOne() {
        // Arrange
//        String couponId = "123";
//        Coupons coupon = new Coupons();
//        coupon.setLimit("2");
//
//        // Mocking to return specific coupon
//        when(adminService.getCouponById(couponId)).thenReturn(coupon);
//
//        // Act
//        Boolean result = mobilePlanServiceImpl.updateLimit(couponId);
//
//        // Assert
//        assertTrue(result);
//        verify(adminService).updateCouponById(couponId, coupon);  // verify if updateCouponById was called
//        assertEquals("1", coupon.getLimit());  // assert that limit was decreased
    }

}
