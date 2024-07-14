package com.excitel.serviceimpl.admin;

import com.excitel.dynamodbqueryhelp.QueryCouponsHelper;
import com.excitel.dynamodbqueryhelp.QueryHelper;
import com.excitel.dynamodbqueryhelp.QueryWrapper;
import com.excitel.exception.custom.DatabaseConnectionException;
import com.excitel.exception.custom.ResourceNotFoundException;
import com.excitel.model.Coupons;
import com.excitel.model.MobilePlan;
import com.excitel.redishelper.MobilePlanRedis;
import com.excitel.serviceimpl.user.MobilePlanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.exception.SdkException;

import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

import static com.excitel.constant.AppConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private MobilePlanServiceImpl mobilePlanService;
   @Mock
   private DynamoDbClient dynamoDbClient;

    @Mock
    private QueryHelper queryHelper;

    @Mock
    private QueryCouponsHelper queryCouponsHelper;
    @Mock
    private MobilePlanRedis mobilePlanRedis;
    @Mock
    private QueryWrapper queryWrapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private MobilePlan testMobilePlan;
    private Coupons testCoupon;
//    @Autowired
//    private AdminServiceImpl adminServiceImpl;







    @BeforeEach
    void setUp() {
        testMobilePlan = new MobilePlan();
        testMobilePlan.setPlanId(UUID.randomUUID().toString());
        testMobilePlan.setPlanType("testType");
        MockitoAnnotations.initMocks(this);
        testCoupon = new Coupons();
        testCoupon.setCouponId(UUID.randomUUID().toString());
        testCoupon.setType("testType");
    }

    @Test
    void addMobilePlan_SuccessfulResponse() {
        // Mock the behavior of queryHelper.addQuery() to return a PutItemRequest
        when(queryHelper.addQuery(any())).thenReturn(PutItemRequest.builder().build());

        // Mock the behavior of dynamoDbClient.putItem() to return a successful response
        when(dynamoDbClient.putItem(any(PutItemRequest.class)))
                .thenReturn((PutItemResponse) PutItemResponse.builder().sdkHttpResponse(
                        SdkHttpResponse.builder().statusCode(200).build()).build());

        // Mock the behavior of queryHelper.getItemByPlanId() to return a GetItemRequest
        when(queryHelper.getItemByPlanId(anyString(), anyString())).thenReturn(GetItemRequest.builder().build());

        // Mock the behavior of dynamoDbClient.getItem() to return a successful response
        Map<String, AttributeValue> item = new HashMap<>();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(item).build());

        // Mock the behavior of queryWrapper.mapTomobilePlan() to return the expected mobile plan
        when(queryWrapper.mapTomobilePlan(anyMap())).thenReturn(testMobilePlan);

        // Mock the behavior of mobilePlanRedis.invalidateMobilePlanCache() to do nothing

        // Call the method under test
        MobilePlan addedPlan = adminService.addMobilePlan(testMobilePlan);

        // Assert that the returned plan matches the expected plan
        assertNotNull(addedPlan);
        assertEquals(testMobilePlan, addedPlan);

        // Verify that the necessary methods were called with the expected arguments
        verify(queryHelper).addQuery(any());
        verify(dynamoDbClient).putItem(any(PutItemRequest.class));
        verify(queryHelper).getItemByPlanId(testMobilePlan.getPlanId(), testMobilePlan.getPlanType());
        verify(dynamoDbClient).getItem(any(GetItemRequest.class));
        verify(queryWrapper).mapTomobilePlan(anyMap());
        verifyNoMoreInteractions(queryHelper, dynamoDbClient, queryWrapper);
    }

    @Test
    void addMobilePlan_DatabaseConnectionException() {
        when(queryHelper.addQuery(any())).thenReturn(PutItemRequest.builder().build());
        when(dynamoDbClient.putItem((PutItemRequest) any())).thenThrow(SdkException.builder().message("Connection failed").build());

        assertThrows(DatabaseConnectionException.class, () -> adminService.addMobilePlan(testMobilePlan));
    }



    @Test
    void addMobilePlan_NullResponse() {
        // Mock the behavior of queryHelper.addQuery() to return a PutItemRequest
        when(queryHelper.addQuery(any())).thenReturn(PutItemRequest.builder().build());

        // Mock the behavior of dynamoDbClient.putItem() to throw an exception
        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenThrow(SdkException.builder().message("Put failed").build());

        // Assert that a ResourceNotFoundException is thrown when addMobilePlan is called
        assertThrows(DatabaseConnectionException.class, () -> adminService.addMobilePlan(testMobilePlan));
    }

    @Test
    void testAddMobilePlan_UnsuccessfulPut() {
        MobilePlan mobilePlan = new MobilePlan(); // Fill with test data

        String uuid = "123"; // Random UUID. You might want to mock UUID as well
        mobilePlan.setPlanId(uuid);

        PutItemRequest putRequest = PutItemRequest.builder().build(); // Mock request
        when(queryHelper.addQuery(mobilePlan)).thenReturn(putRequest);

        PutItemResponse putResponse = (PutItemResponse) PutItemResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder().statusCode(400).build()) // Unsuccessful response
                .build();
        when(dynamoDbClient.putItem(putRequest)).thenReturn(putResponse);

        MobilePlan result = adminService.addMobilePlan(mobilePlan);
        assertNull(result);
    }

    @Test
    void testAddMobilePlan() {
        MobilePlan mobilePlan = new MobilePlan(); // Fill with test data

        String uuid = "123"; // Random UUID. You might want to mock UUID as well
        mobilePlan.setPlanId(uuid);

        PutItemRequest putRequest = PutItemRequest.builder().build(); // Mock request
        when(queryHelper.addQuery(mobilePlan)).thenReturn(putRequest);

        PutItemResponse putResponse = (PutItemResponse) PutItemResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder().statusCode(200).build())
                .build();
        when(dynamoDbClient.putItem(putRequest)).thenReturn(putResponse);

        GetItemRequest getRequest = GetItemRequest.builder().build(); // Mock request
        queryHelper.getItemByPlanId("08a8eff5-815b-4446-bb4f-feba663cec4d", null);
        when(queryHelper.getItemByPlanId(anyString(), eq(null))).thenReturn(getRequest);
        Map<String, AttributeValue> item = new HashMap<>(); // Fill with test data
        GetItemResponse getResponse = GetItemResponse.builder().item(item).build();
        when(dynamoDbClient.getItem(getRequest)).thenReturn(getResponse);

        when(queryWrapper.mapTomobilePlan(item)).thenReturn(mobilePlan);

        doNothing().when(mobilePlanRedis).invalidateMobilePlanCache();

        MobilePlan result = adminService.addMobilePlan(mobilePlan);
        assertEquals(mobilePlan, result);
    }

    @Test
    void updateMobilePlan_ExceptionThrown() {
        when(queryHelper.updateQuery(any(), anyString())).thenReturn(UpdateItemRequest.builder().build());
        when(dynamoDbClient.updateItem((UpdateItemRequest) any())).thenThrow(SdkException.builder().message("Update failed").build());

        assertThrows(SdkException.class, () -> adminService.updateMobilePlan(testMobilePlan.getPlanId(), testMobilePlan));
    }

    @Test
    void testUpdateMobilePlan_itemNotNullNotEmpty() {
        // Given
        String planId = "planId";
        MobilePlan mobilePlan = new MobilePlan();
        mobilePlan.setPlanType("SomePlanType"); // Set this to a real planType, if needed
        Map<String, AttributeValue> updatedItem = new HashMap<>();
        updatedItem.put("key", AttributeValue.builder().s("value").build());

        UpdateItemRequest updateItemRequest = Mockito.mock(UpdateItemRequest.class); // add this

        // Given
        UpdateItemResponse updateItemResponse = (UpdateItemResponse) UpdateItemResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder()
                        .statusCode(200) // Assuming the update is successful
                        .build())
                .build();
        when(queryHelper.updateQuery(mobilePlan, planId)).thenReturn(updateItemRequest); // add this
        when(dynamoDbClient.updateItem(updateItemRequest)).thenReturn(updateItemResponse);

        GetItemRequest getItemRequest = Mockito.mock(GetItemRequest.class); // add this
        when(queryHelper.getItemByPlanId(planId, mobilePlan.getPlanType())).thenReturn(getItemRequest); // add this
        when(dynamoDbClient.getItem(getItemRequest)).thenReturn(GetItemResponse.builder().item(updatedItem).build());

        when(queryWrapper.mapTomobilePlan(updatedItem)).thenReturn(mobilePlan); // add this

        // When
        MobilePlan result = adminService.updateMobilePlan(planId, mobilePlan);

        // Then
        verify(mobilePlanRedis, times(1)).invalidateMobilePlanCache();
        verify(queryWrapper, times(1)).mapTomobilePlan(updatedItem);
        assertNotNull(result);
    }

    @Test
    void deleteMobilePlan_NullResponse() {
        Map<String, AttributeValue> mockItem = new HashMap<>();
        mockItem.put("planId", AttributeValue.builder().s(testMobilePlan.getPlanId()).build());
        when(dynamoDbClient.getItem((GetItemRequest) any())).thenReturn(GetItemResponse.builder().item(mockItem).build());
        when(queryWrapper.mapTomobilePlan(any())).thenReturn(testMobilePlan);
        when(queryHelper.deleteQuery(anyString(), any())).thenReturn(UpdateItemRequest.builder().build());
        when(dynamoDbClient.updateItem((UpdateItemRequest) any())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> adminService.deleteMobilePlan(testMobilePlan.getPlanId(), testMobilePlan.getPlanType()));
    }
    @Test
    void testDeleteMobilePlan_responseNotNull() {
        // Given
        String planId = "planId";
        String planType = "SomePlanType";
        Map<String, AttributeValue> mobilePlan = new HashMap<>();
        mobilePlan.put("key", AttributeValue.builder().s("value").build());

        GetItemRequest getItemRequest = Mockito.mock(GetItemRequest.class);
        when(queryHelper.getItemByPlanId(planId, planType)).thenReturn(getItemRequest);
        when(dynamoDbClient.getItem(getItemRequest)).thenReturn(GetItemResponse.builder().item(mobilePlan).build());

        MobilePlan mappedMobilePlan = new MobilePlan();
        when(queryWrapper.mapTomobilePlan(mobilePlan)).thenReturn(mappedMobilePlan);

        UpdateItemRequest deleteItemRequest = Mockito.mock(UpdateItemRequest.class);
        when(queryHelper.deleteQuery(planId, mappedMobilePlan)).thenReturn(deleteItemRequest);
        when(dynamoDbClient.updateItem(deleteItemRequest)).thenReturn(UpdateItemResponse.builder().build());

        // When
        String result = adminService.deleteMobilePlan(planId, planType);

        // Then
        verify(mobilePlanRedis, times(1)).invalidateMobilePlanCache();
        assertEquals(planId + " Deleted successfully", result);
    }

    @Test
    void testDeleteMobilePlan_mobilePlanEmpty() {
        // Given
        String planId = "planId";
        String planType = "SomePlanType";
        Map<String, AttributeValue> mobilePlan = new HashMap<>();

        GetItemRequest getItemRequest = Mockito.mock(GetItemRequest.class);
        when(queryHelper.getItemByPlanId(planId, planType)).thenReturn(getItemRequest);
        when(dynamoDbClient.getItem(getItemRequest)).thenReturn(GetItemResponse.builder().item(mobilePlan).build());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.deleteMobilePlan(planId, planType);
        });

        // Then
        assertEquals("No plan found with id: " + planId, exception.getMessage());
    }


    @Test
    void createCoupons_DatabaseConnectionException() {
        when(queryCouponsHelper.addCouponQuery(any())).thenReturn(PutItemRequest.builder().build());
        when(dynamoDbClient.putItem((PutItemRequest) any())).thenThrow(SdkException.builder().message("Connection failed").build());

        assertThrows(DatabaseConnectionException.class, () -> adminService.createCoupons(testCoupon));
    }

    @Test
    void testCreateCoupons_UnsuccessfulPut() {
        Coupons coupons = new Coupons(); // Fill with test data

        String uuid = "123"; // Random UUID. You might want to mock UUID as well
        coupons.setCouponId(uuid);

        PutItemRequest putRequest = PutItemRequest.builder().build(); // Mock request
        when(queryCouponsHelper.addCouponQuery(coupons)).thenReturn(putRequest);

        PutItemResponse putResponse = (PutItemResponse) PutItemResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder().statusCode(400).build()) // Unsuccessful response
                .build();
        when(dynamoDbClient.putItem(putRequest)).thenReturn(putResponse);

        Coupons result = adminService.createCoupons(coupons);
        assertNull(result);
    }

    @Test
    void testGetCouponById_validCoupon() {
        // Given
        String couponId = "couponId";
        Map<String, AttributeValue> addedItem = new HashMap<>();
        addedItem.put("key", AttributeValue.builder().s("value").build());

        GetItemRequest getItemRequest = Mockito.mock(GetItemRequest.class);
        when(queryCouponsHelper.getItemByCouponId(INTERNAL.getValue(), couponId)).thenReturn(getItemRequest);
        when(dynamoDbClient.getItem(getItemRequest)).thenReturn(GetItemResponse.builder().item(addedItem).build());

        Coupons expectedCoupon = new Coupons(); // Replace with your actual Coupons object creation
        when(queryWrapper.mapToCoupons(addedItem)).thenReturn(expectedCoupon);

        // When
        Coupons result = adminService.getCouponById(couponId);

        // Then
        assertNotNull(result);
        assertEquals(expectedCoupon, result);
    }

    @Test
    void testGetCouponById_invalidCoupon() {
        // Given
        String couponId = "couponId";
        Map<String, AttributeValue> addedItem = new HashMap<>();

        GetItemRequest getItemRequest = Mockito.mock(GetItemRequest.class);
        when(queryCouponsHelper.getItemByCouponId(INTERNAL.getValue(), couponId)).thenReturn(getItemRequest);
        when(dynamoDbClient.getItem(getItemRequest)).thenReturn(GetItemResponse.builder().item(addedItem).build());

        // When
        Coupons result = adminService.getCouponById(couponId);

        // Then
        assertNull(result);
    }

    @Test
    void testGetCouponById_dynamoDbClientThrowsException() {
        // Given
        String couponId = "couponId";
        GetItemRequest getItemRequest = Mockito.mock(GetItemRequest.class);
        when(queryCouponsHelper.getItemByCouponId(INTERNAL.getValue(), couponId)).thenReturn(getItemRequest);

        // Make dynamoDbClient.getItem(getRequest) throw an SdkException
        when(dynamoDbClient.getItem(getItemRequest)).thenThrow(SdkException.builder().message("Test exception").build());

        // When
        Exception exception = assertThrows(DatabaseConnectionException.class, () -> {
            adminService.getCouponById(couponId);
        });

        // Then
        assertTrue(exception.getMessage().contains("Test exception"));
    }


    @Test
    void testGetCouponByIdForUser_returnsValidCoupons() {
        // Given
        String couponId = "couponId";
        Map<String, AttributeValue> fetchedItem = new HashMap<>();
        fetchedItem.put("key", AttributeValue.builder().s("value").build());

        GetItemRequest getItemRequest = Mockito.mock(GetItemRequest.class);
        when(queryCouponsHelper.getItemByCouponId(INTERNAL.getValue(), couponId)).thenReturn(getItemRequest);
        when(dynamoDbClient.getItem(getItemRequest)).thenReturn(GetItemResponse.builder().item(fetchedItem).build());

        Coupons expectedCoupons = new Coupons();
        when(queryWrapper.mapToCouponsForUser(fetchedItem)).thenReturn(expectedCoupons);

        // When
        Coupons result = adminService.getCouponByIdForUser(couponId);

        // Then
        assertEquals(expectedCoupons, result);
    }

    @Test
    void testGetCouponByIdForUser_throwsDatabaseConnectionException() {
        // Given
        String couponId = "couponId";

        GetItemRequest getItemRequest = Mockito.mock(GetItemRequest.class);
        when(queryCouponsHelper.getItemByCouponId(INTERNAL.getValue(), couponId)).thenReturn(getItemRequest);
        when(dynamoDbClient.getItem(getItemRequest)).thenThrow(SdkException.builder().message("Error message").build());

        // When
        Exception exception = assertThrows(DatabaseConnectionException.class, () -> {
            adminService.getCouponByIdForUser(couponId);
        });

        // Then
        assertEquals(DYNAMODB_ERROR.getValue() + "Error message", exception.getMessage());
    }

    @Test
    void testDeleteMobilePlan_exceptionThrown() {
        // Given
        String planId = "planId";
        String planType = "SomePlanType";

        GetItemRequest getItemRequest = Mockito.mock(GetItemRequest.class);
        when(queryHelper.getItemByPlanId(planId, planType)).thenReturn(getItemRequest);

        // Mock dynamoDbClient to throw an exception when getItem is called
        when(dynamoDbClient.getItem(getItemRequest)).thenThrow(new RuntimeException("Test exception"));

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.deleteMobilePlan(planId, planType);
        });

        // Then
        assertEquals("No plan found with id: " + planId, exception.getMessage());
    }




    private MobilePlan getMobilePlan() {
        return MobilePlan.builder()
                .planId("123")
                .planType("Prepaid")
                .validity("28")
                .price("210")
                .category("Unlimited")
                .build();
    }


    @Test
    void updateMobilePlan_PlanNotFound() {
        // Mock data
        String planId = "nonExistingPlanId";
        MobilePlan mobilePlan = new MobilePlan();
        mobilePlan.setPlanId(planId);

        // Mock UpdateItemRequest and GetItemRequest
        UpdateItemRequest updateRequest = UpdateItemRequest.builder().build();
        GetItemRequest getRequest = GetItemRequest.builder().build();

        // Mock queryHelper methods
        when(queryHelper.updateQuery(mobilePlan, planId)).thenReturn(updateRequest);
        when(queryHelper.getItemByPlanId(planId, mobilePlan.getPlanType())).thenReturn(getRequest);

        // Mock DynamoDB response for updateItem
        UpdateItemResponse updateResponse = (UpdateItemResponse) UpdateItemResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder().statusCode(200).build())
                .build();
        when(dynamoDbClient.updateItem(updateRequest)).thenReturn(updateResponse);

        // Mock DynamoDB response for getItem
        GetItemResponse getItemResponse = GetItemResponse.builder()
                .item(Collections.emptyMap())
                .build();
        when(dynamoDbClient.getItem(getRequest)).thenReturn(getItemResponse);

        // Call the method and verify
        assertThrows(ResourceNotFoundException.class, () -> adminService.updateMobilePlan(planId, mobilePlan));
    }

    @Test
    void testCreateCoupons_responseIsSuccessful() {
        // Given
        Coupons coupons = new Coupons();
        coupons.setType("SomeType"); // Set this to a real type, if needed
        Map<String, AttributeValue> addedItem = new HashMap<>();
        addedItem.put("key", AttributeValue.builder().s("value").build());

        PutItemRequest putItemRequest = Mockito.mock(PutItemRequest.class); // add this
        // Assuming that the uuid is generated inside the addCouponQuery method
        when(queryCouponsHelper.addCouponQuery(coupons)).thenReturn(putItemRequest); // add this

        // Given
        PutItemResponse putItemResponse = (PutItemResponse) PutItemResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder()
                        .statusCode(200) // Assuming the put is successful
                        .build())
                .build();
        when(dynamoDbClient.putItem(putItemRequest)).thenReturn(putItemResponse);

        GetItemRequest getItemRequest = Mockito.mock(GetItemRequest.class); // add this

//        when(queryCouponsHelper.getItemByCouponId(coupons.getType(), coupons.getCouponId())).thenReturn(getItemRequest); // add this
        when(queryCouponsHelper.getItemByCouponId(eq("SomeType"), anyString())).thenReturn(getItemRequest);
        when(dynamoDbClient.getItem(getItemRequest)).thenReturn(GetItemResponse.builder().item(addedItem).build());

        when(queryWrapper.mapToCoupons(addedItem)).thenReturn(coupons); // add this

        // When
        Coupons result = adminService.createCoupons(coupons);

        // Then
        verify(queryWrapper, times(1)).mapToCoupons(addedItem);
        assertNotNull(result);
        assertEquals(coupons, result);
    }

    @Test
    void getAllCoupons_Success() {
        // Mock QueryRequest
        QueryRequest getAllCouponsRequest = QueryRequest.builder().build();

        // Mock QueryResponse
        Map<String, AttributeValue> item1 = new HashMap<>();
        item1.put("couponId", AttributeValue.builder().s("coupon1").build());
        Map<String, AttributeValue> item2 = new HashMap<>();
        item2.put("couponId", AttributeValue.builder().s("coupon2").build());
        List<Map<String, AttributeValue>> items = List.of(item1, item2);
        QueryResponse queryResponse = (QueryResponse) QueryResponse.builder()
                .items(items)
                .sdkHttpResponse(SdkHttpResponse.builder().statusCode(200).build())
                .build();

        // Mock DynamoDB behavior
        when(queryCouponsHelper.getAllCouponsByType("Internal")).thenReturn(getAllCouponsRequest);
        when(dynamoDbClient.query(getAllCouponsRequest)).thenReturn(queryResponse);

        // Mock mapping behavior
        Coupons coupon1 = new Coupons();
        Coupons coupon2 = new Coupons();
        when(queryWrapper.mapToCoupons(item1)).thenReturn(coupon1);
        when(queryWrapper.mapToCoupons(item2)).thenReturn(coupon2);

        // Call the method
        List<Coupons> result = adminService.getAllCoupons();

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(coupon1, result.get(0));
        assertEquals(coupon2, result.get(1));
    }

    @Test
    void getAllCoupons_DatabaseConnectionException() {
        // Mock QueryRequest
        QueryRequest getAllCouponsRequest = QueryRequest.builder().build();

        // Mock SdkException
        SdkException sdkException = SdkException.builder().message("Connection failed").build();

        // Mock DynamoDB behavior
        when(queryCouponsHelper.getAllCouponsByType("Internal")).thenReturn(getAllCouponsRequest);
        when(dynamoDbClient.query(getAllCouponsRequest)).thenThrow(sdkException);

        // Call the method and assert exception
        DatabaseConnectionException exception = assertThrows(DatabaseConnectionException.class, () -> adminService.getAllCoupons());
        assertEquals("Error occurred while connecting to DynamoDB Connection failed", exception.getMessage());
    }




    @Test
    void deleteCouponById_Success() {
        Map<String, AttributeValue> mockItem = new HashMap<>();
        mockItem.put("couponId", AttributeValue.builder().s(testCoupon.getCouponId()).build());
        when(dynamoDbClient.getItem((GetItemRequest) any())).thenReturn(GetItemResponse.builder().item(mockItem).build());
        when(queryWrapper.mapToCoupons(any())).thenReturn(testCoupon);
        when(queryCouponsHelper.deleteCouponQuery(anyString(), anyString())).thenReturn(DeleteItemRequest.builder().build());
        when(dynamoDbClient.deleteItem((DeleteItemRequest) any())).thenReturn(DeleteItemResponse.builder().build());

        String result = adminService.deleteCouponById( testCoupon.getCouponId());

        assertEquals("Coupon deleted successfully", result);
    }

    @Test
    void deleteCouponById_NotFound() {
        when(dynamoDbClient.getItem((GetItemRequest) any())).thenReturn(GetItemResponse.builder().item(Collections.emptyMap()).build());

        assertThrows(ResourceNotFoundException.class, () -> adminService.deleteCouponById( testCoupon.getCouponId()));
    }


    @Test
    void testUpdateCouponById_NotFound() {
        String couponId = "123";
        Coupons updatedCoupon = new Coupons(); // Fill with test data

        UpdateItemRequest updateRequest = UpdateItemRequest.builder().build(); // Mock request
        when(queryCouponsHelper.updateQuery(updatedCoupon, couponId)).thenReturn(updateRequest);

        UpdateItemResponse updateResponse = (UpdateItemResponse) UpdateItemResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder().statusCode(200).build())
                .build();
        when(dynamoDbClient.updateItem(updateRequest)).thenReturn(updateResponse);

        GetItemRequest getRequest = GetItemRequest.builder().build(); // Mock request
        when(queryCouponsHelper.getItemByCouponId(updatedCoupon.getType(), couponId)).thenReturn(getRequest);

        Map<String, AttributeValue> item = new HashMap<>(); // Empty map indicates coupon not found
        GetItemResponse getResponse = GetItemResponse.builder().item(item).build();
        when(dynamoDbClient.getItem(getRequest)).thenReturn(getResponse);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.updateCouponById(couponId, updatedCoupon);
        });

        String expectedMessage = "No coupon found with id: " + couponId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeleteCouponById_CouponNotFound() {
        String couponId = "123";

        GetItemRequest getRequest = GetItemRequest.builder().build(); // Mock request
        when(queryCouponsHelper.getItemByCouponId(INTERNAL.getValue(), couponId)).thenReturn(getRequest);

        Map<String, AttributeValue> item = new HashMap<>(); // Empty map indicates coupon not found
        GetItemResponse getResponse = GetItemResponse.builder().item(item).build();
        when(dynamoDbClient.getItem(getRequest)).thenReturn(getResponse);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.deleteCouponById(couponId);
        });

        String expectedMessage = "No coupon found with id: " + couponId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeleteCouponById_DeleteUnsuccessful() {
        String couponId = "123";

        GetItemRequest getRequest = GetItemRequest.builder().build(); // Mock request
        lenient().when(queryCouponsHelper.getItemByCouponId(INTERNAL.getValue(), couponId)).thenReturn(getRequest);

        Map<String, AttributeValue> item = new HashMap<>(); // Fill with test data
        GetItemResponse getResponse = GetItemResponse.builder().item(item).build();
        lenient().when(dynamoDbClient.getItem(getRequest)).thenReturn(getResponse);

        Coupons coupons = new Coupons(); // Fill with test data
        lenient().when(queryWrapper.mapToCoupons(item)).thenReturn(coupons);

        DeleteItemRequest deleteRequest = DeleteItemRequest.builder().build(); // Mock request
        lenient().when(queryCouponsHelper.deleteCouponQuery(couponId, coupons.getType())).thenReturn(deleteRequest);

        lenient().when(dynamoDbClient.deleteItem(deleteRequest)).thenReturn(null); // Return null to simulate unsuccessful delete

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.deleteCouponById(couponId);
        });

        String expectedMessage = NO_COUPON_EX_MSG.getValue() + couponId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }



}