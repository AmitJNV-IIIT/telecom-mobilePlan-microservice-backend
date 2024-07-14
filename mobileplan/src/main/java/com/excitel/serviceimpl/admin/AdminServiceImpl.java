package com.excitel.serviceimpl.admin;

import com.excitel.dynamodbqueryhelp.QueryCouponsHelper;
import com.excitel.dynamodbqueryhelp.QueryHelper;
import com.excitel.dynamodbqueryhelp.QueryWrapper;
import com.excitel.exception.custom.DatabaseConnectionException;
import com.excitel.exception.custom.ResourceNotFoundException;
import com.excitel.model.Coupons;
import com.excitel.model.MobilePlan;
import com.excitel.redishelper.MobilePlanRedis;
import com.excitel.service.admin.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

import static com.excitel.constant.AppConstants.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired//NOSONAR
    private QueryHelper queryHelper;
    @Autowired//NOSONAR
    private QueryWrapper queryWrapper;
    @Autowired//NOSONAR
    private QueryCouponsHelper queryCouponsHelper;
    @Autowired//NOSONAR
    private DynamoDbClient dynamoDbClient;

    @Autowired //NOSONAR
    private MobilePlanRedis mobilePlanRedis;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    /**
     * Adds a new mobile plan.
     *
     * @param mobilePlan The mobile plan to add.
     * @return The added mobile plan.
     * @throws DatabaseConnectionException If an error occurs while connecting to the database.
     */
    @Override
    public MobilePlan addMobilePlan(MobilePlan mobilePlan) {
        try {
            String uuid= String.valueOf(UUID.randomUUID());
            mobilePlan.setPlanId(uuid);
            PutItemRequest request = queryHelper.addQuery(mobilePlan);
            PutItemResponse response = dynamoDbClient.putItem(request);
            if (response.sdkHttpResponse().isSuccessful()) {
                GetItemRequest getRequest = queryHelper.getItemByPlanId(uuid,mobilePlan.getPlanType());
                Map<String, AttributeValue> addedItem = dynamoDbClient.getItem(getRequest).item();
                // Delete the cached data as state of db has changed
                mobilePlanRedis.invalidateMobilePlanCache();
                return queryWrapper.mapTomobilePlan(addedItem);
            }else{
                return null;
            }
        } catch (SdkException e) {
            throw new DatabaseConnectionException(DYNAMODB_ERROR.getValue()+e.getMessage());
        }
    }

    @Override
    public MobilePlan updateMobilePlan(String planId, MobilePlan mobilePlan) {
        UpdateItemRequest request = queryHelper.updateQuery(mobilePlan, planId);
        UpdateItemResponse response = dynamoDbClient.updateItem(request);
        Map<String, AttributeValue> updatedItem = null;
        if (response.sdkHttpResponse().isSuccessful()) {
            GetItemRequest getRequest = queryHelper.getItemByPlanId(planId,mobilePlan.getPlanType());
            updatedItem = dynamoDbClient.getItem(getRequest).item();
        }
        if (updatedItem != null && !updatedItem.isEmpty()) {
            // Delete the cached data as state of db has changed
            mobilePlanRedis.invalidateMobilePlanCache();
            return queryWrapper.mapTomobilePlan(updatedItem);
        } else {
            LOGGER.warn("Mobile plan not found with ID: {}", planId);
            throw new ResourceNotFoundException(NO_PLAN_FOUND_EX_MSG.getValue()+planId);
        }
    }
    /**
     * Deletes a mobile plan.
     *
     * @param planId The ID of the mobile plan to delete.
     * @return A message indicating the result of the deletion operation.
     */
    @Override
    public String deleteMobilePlan(String planId, String planType) {
        try {
            GetItemRequest getRequest = queryHelper.getItemByPlanId(planId,planType);
            Map<String, AttributeValue> mobilePlan = dynamoDbClient.getItem(getRequest).item();

            if(mobilePlan.isEmpty()){
                throw new ResourceNotFoundException(NO_PLAN_FOUND_EX_MSG.getValue() + planId);
            }

            MobilePlan mobilePlan1 = queryWrapper.mapTomobilePlan(mobilePlan);

            UpdateItemRequest request = queryHelper.deleteQuery(planId, mobilePlan1);
            UpdateItemResponse response = dynamoDbClient.updateItem(request);

            if (response != null) {
                // Delete the cached data as state of db has changed
                mobilePlanRedis.invalidateMobilePlanCache();
                return planId + " Deleted successfully";
            } else {
                throw new ResourceNotFoundException(NO_PLAN_FOUND_EX_MSG.getValue() + planId);
            }

        } catch (Exception e) {
            LOGGER.error("Error occurred while deleting mobile plan with ID: {}", planId, e);
            throw new ResourceNotFoundException(NO_PLAN_FOUND_EX_MSG.getValue() + planId);
        }
    }

    /**
     * @return
     */
    @Override
    public List<Coupons> getAllCoupons() {
        List<Coupons> allCoupons = new ArrayList<>();
        try {
            QueryRequest getAllCouponsRequest = queryCouponsHelper.getAllCouponsByType(INTERNAL.getValue());
            QueryResponse response = dynamoDbClient.query(getAllCouponsRequest);

            if (response.sdkHttpResponse().isSuccessful()) {
                for (Map<String, AttributeValue> item : response.items()) {
                    Coupons coupon = queryWrapper.mapToCoupons(item);
                    allCoupons.add(coupon);
                }
            }
        } catch (SdkException e) {
            throw new DatabaseConnectionException("Error occurred while connecting to DynamoDB " + e.getMessage());
        }
        return allCoupons;
    }

    /**
     * @param couponId
     * @return
     */
    @Override
    public Coupons getCouponById(String couponId) {
        try {
            GetItemRequest getRequest = queryCouponsHelper.getItemByCouponId(INTERNAL.getValue(), couponId);
            Map<String, AttributeValue> addedItem = dynamoDbClient.getItem(getRequest).item();
            return addedItem == null || addedItem.isEmpty() ? null : queryWrapper.mapToCoupons(addedItem);
        } catch (SdkException e) {
            // Handle DynamoDB connection or operation errors
            throw new DatabaseConnectionException(DYNAMODB_ERROR.getValue() + e.getMessage());
        }
    }

    /**
     * @param couponId
     * @return
     */
    @Override
    public Coupons getCouponByIdForUser(String couponId) {
        try {
            GetItemRequest getRequest = queryCouponsHelper.getItemByCouponId(INTERNAL.getValue(), couponId);
            Map<String, AttributeValue> fetchedItem = dynamoDbClient.getItem(getRequest).item();
            return queryWrapper.mapToCouponsForUser(fetchedItem);
        } catch (SdkException e) {
            // Handle DynamoDB connection or operation errors
            throw new DatabaseConnectionException(DYNAMODB_ERROR.getValue()+ e.getMessage());
        }
    }

    /**
     * Creates a new coupon.
     *
     * @param coupons The coupon to create.
     * @return The created coupon.
     * @throws DatabaseConnectionException If an error occurs while connecting to the database.
     */
    @Override
    public Coupons createCoupons(Coupons coupons){
        try {
            String uuid= String.valueOf(UUID.randomUUID());
            coupons.setCouponId(uuid);
            PutItemRequest request = queryCouponsHelper.addCouponQuery(coupons);
            PutItemResponse response = dynamoDbClient.putItem(request);
            if (response.sdkHttpResponse().isSuccessful()) {
                GetItemRequest getRequest = queryCouponsHelper.getItemByCouponId(coupons.getType(),uuid);
                Map<String, AttributeValue> addedItem = dynamoDbClient.getItem(getRequest).item();
                return queryWrapper.mapToCoupons(addedItem);
            }else{
                return null;
            }
        } catch (SdkException e) {
            throw new DatabaseConnectionException("Error occurred while connecting to DynamoDB "+e.getMessage());
        }
    }
    /**
     * Updates an existing coupon.
     *
     * @param couponId     The ID of the coupon to update.
     * @param updatedCoupon The updated coupon details.
     * @return The updated coupon.
     */
    @Override
    public Coupons updateCouponById(String couponId, Coupons updatedCoupon) {

        UpdateItemRequest request = queryCouponsHelper.updateQuery(updatedCoupon, couponId);
        UpdateItemResponse response = dynamoDbClient.updateItem(request);
        Map<String, AttributeValue> updatedItem = null;
        if (response.sdkHttpResponse().isSuccessful()) {
            GetItemRequest getRequest = queryCouponsHelper.getItemByCouponId(updatedCoupon.getType(),couponId);
            updatedItem = dynamoDbClient.getItem(getRequest).item();
        }
        if (updatedItem != null && !updatedItem.isEmpty()) {
            return queryWrapper.mapToCoupons(updatedItem);
        } else {
            LOGGER.warn("coupon not found with ID: {}", couponId);
            throw new ResourceNotFoundException(NO_COUPON_EX_MSG.getValue() + couponId);
        }

    }
    /**
     * Deletes a coupon.
     *
     * @param couponId The ID of the coupon to delete.
     * @return A message indicating the result of the deletion operation.
     */
    @Override
    public String deleteCouponById(String couponId) {
        try {
            GetItemRequest getRequest = queryCouponsHelper.getItemByCouponId(INTERNAL.getValue(),couponId);
            Map<String, AttributeValue> couponItem = dynamoDbClient.getItem(getRequest).item();

            if(couponItem.isEmpty()){
                throw new ResourceNotFoundException("Not Found");

            }

            Coupons coupons = queryWrapper.mapToCoupons(couponItem);

            DeleteItemRequest request = queryCouponsHelper.deleteCouponQuery(couponId, coupons.getType());
            DeleteItemResponse response = dynamoDbClient.deleteItem(request);

            if (response != null) {
                return "Coupon deleted successfully";
            } else {
                throw new ResourceNotFoundException(NO_COUPON_EX_MSG.getValue() + couponId);
            }

        } catch (Exception e) {
            LOGGER.error("Error occurred while deleting coupon with ID: {}", couponId, e);
            throw new ResourceNotFoundException(NO_COUPON_EX_MSG.getValue() + couponId);
        }
    }



}
