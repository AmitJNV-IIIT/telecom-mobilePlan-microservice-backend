package com.excitel.serviceimpl.user;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.excitel.dto.RequestDTO;

import com.excitel.dto.CouponResponseDTO;
import com.excitel.dynamodbqueryhelp.QueryCouponsHelper;
import com.excitel.dynamodbqueryhelp.QueryWrapper;
import com.excitel.exception.custom.DatabaseConnectionException;
import com.excitel.model.Coupons;
import com.excitel.model.MobilePlan;
import com.excitel.exception.custom.ResourceNotFoundException;
import com.excitel.redishelper.MobilePlanRedis;
import com.excitel.service.user.MobilePlanService;
import com.excitel.serviceimpl.admin.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of the MobilePlanService interface for managing mobile plans.
 */
@Service
public class MobilePlanServiceImpl implements MobilePlanService {

    @Autowired //NOSONAR
    private AdminServiceImpl adminService;

    @Autowired//NOSONAR
    private AmazonDynamoDB amazonDynamoDB;


    @Autowired//NOSONAR
    private QueryWrapper queryWrapper;
    @Autowired//NOSONAR
    private QueryCouponsHelper queryCouponsHelper;
    @Autowired//NOSONAR
    private DynamoDbClient dynamoDbClient;

    @Autowired //NOSONAR
    private MobilePlanRedis mobilePlanRedis;

    /**
     * Retrieves mobile plans based on provided parameters.
     *
     * @param params The request parameters.
     * @return A list of mobile plans.
     */
    @Override
    public List<MobilePlan> getMobilePlanWithQuery(RequestDTO params) {
        String active = params.getActive();
        String planId = params.getPlanId();
        String type = params.getType();
        String category = params.getCategory();
        String data = params.getData();
        String speed = params.getDays();
        Integer offset = params.getOffset();
        Integer limit = params.getLimit();

        String key = mobilePlanRedis.generateKeyFromParams(params);
        List<MobilePlan> mobilePlans = mobilePlanRedis.getMobilePlansCache(key);

        // Make a DB call only when the required data is not found in cache
        if (mobilePlans == null || mobilePlans.isEmpty()) {
            mobilePlans = new ArrayList<>();

            QueryRequest queryRequest = buildQueryRequest(type, active, planId, category, data, speed);
            QueryResponse queryResponse1 = dynamoDbClient.query(queryRequest);

            for (Map<String, AttributeValue> item : queryResponse1.items()) {
                MobilePlan mobilePlan = queryWrapper.mapTomobilePlan(item);
                mobilePlans.add(mobilePlan);
            }

            if (offset != null && limit != null) {
                mobilePlans = mobilePlans.subList(offset, Math.min(offset + limit, mobilePlans.size()));
            }
            // Also, add the response to the cache
            mobilePlanRedis.addMobilePlansCache(key, mobilePlans);
        }

        return mobilePlans;
    }

    /**
     * Builds a query request for retrieving mobile plans based on provided parameters.
     *
     * @param type     The plan type.
     * @param active   The plan's active status.
     * @param planId   The plan ID.
     * @param category The plan category.
     * @param data     The plan data.
     * @param days     The plan validity in days.
     * @return The built query request.
     */
//will be refactored again
    public QueryRequest buildQueryRequest(String type, String active, String planId, String category, String data, String days)//NOSONAR
    {

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        List<String> filterConditions = new ArrayList<>();
        QueryRequest.Builder queryRequestBuilder = QueryRequest.builder()
                .tableName("plan-table");

        // Handle filter conditions based on provided parameters
        if (planId != null) {//NOSONAR
            expressionAttributeValues.put(":planId", AttributeValue.builder().s(planId).build());
            expressionAttributeValues.put(":planType", AttributeValue.builder().s(type).build());

            queryRequestBuilder.keyConditionExpression("PlanType = :planType AND PlanID = :planId");

            if (active != null) {//NOSONAR
                expressionAttributeValues.put(":active", AttributeValue.builder().s(active).build());
                queryRequestBuilder.filterExpression("Active = :active")
                        .expressionAttributeValues(expressionAttributeValues);
            }
            queryRequestBuilder.expressionAttributeValues(expressionAttributeValues);

        } else { // Use filter expression for other attributes
            if (type != null) {//NOSONAR
                expressionAttributeValues.put(":type", AttributeValue.builder().s(type).build());
                queryRequestBuilder.keyConditionExpression("PlanType = :type");
            }
            if (category != null) {//NOSONAR
                expressionAttributeValues.put(":category", AttributeValue.builder().s(category).build());
                filterConditions.add("Category = :category");
            }
            if (active != null) {//NOSONAR
                expressionAttributeValues.put(":active", AttributeValue.builder().s(active).build());
                filterConditions.add("Active = :active");
            }
            if (days != null) {//NOSONAR
                expressionAttributeValues.put(":days", AttributeValue.builder().s(days).build());
                filterConditions.add("Validity = :days");
            }
            if (data != null) {//NOSONAR
                expressionAttributeValues.put(":data", AttributeValue.builder().s(data).build());
                filterConditions.add("TotalData = :data");
            }

            if (!filterConditions.isEmpty()) {//NOSONAR
                String combinedFilterExpression = String.join(" AND ", filterConditions);

                queryRequestBuilder = queryRequestBuilder.filterExpression(combinedFilterExpression)
                        .expressionAttributeValues(expressionAttributeValues);
            } else {//NOSONAR
                queryRequestBuilder = queryRequestBuilder.expressionAttributeValues(expressionAttributeValues);
            }
        }

        return queryRequestBuilder.build();
    }

    /**
     * Retrieves a coupon by its ID.
     *
     * @param couponId The ID of the coupon.
     * @return The retrieved coupon.
     * @throws ResourceNotFoundException   if the coupon is not found.
     * @throws DatabaseConnectionException if an error occurs while connecting to the database.
     */
    @Override
    public Coupons getByCouponId(String couponId) {
        try {
            GetItemRequest getRequest = queryCouponsHelper.getItemByCouponId("Internal", couponId);
            Map<String, AttributeValue> response = dynamoDbClient.getItem(getRequest).item();
            if (response.isEmpty()) {
                throw new ResourceNotFoundException("Coupon not found with id " + couponId);
            }

            return queryWrapper.mapToCoupons(response);

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No coupon found with id: " + couponId);
        } catch (DatabaseConnectionException e) {
            throw new DatabaseConnectionException("Error while connecting to database");
        }
    }



    /**
     * Retrieves all coupons by a list of coupon IDs.
     *
     * @param couponIds  The list of coupon IDs.
     * @param couponType The coupon type.
     * @return The response containing all coupons.
     */
    @Override
    public CouponResponseDTO getAllCouponByCouponIdList(List<String> couponIds, String couponType) {
        List<Map<String, AttributeValue>> keysToGet = new ArrayList<>();
        for (String couponId : couponIds) {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("CouponID", AttributeValue.builder().s(couponId).build());
            key.put("CouponType", AttributeValue.builder().s(couponType).build());
            keysToGet.add(key);
        }

        KeysAndAttributes keysAndAttributes = KeysAndAttributes.builder()
                .projectionExpression("CouponID, CouponType, CouponCode, Expire, TotalData, Image, Description")
                .keys(keysToGet)
                .build();

        BatchGetItemRequest batchGetItemRequest = BatchGetItemRequest.builder()
                .requestItems(Map.of("coupon-table", keysAndAttributes))
                .build();


        BatchGetItemResponse batchGetItemResponse = dynamoDbClient.batchGetItem(batchGetItemRequest);

        Map<String, Coupons> couponMap = batchGetItemResponse.responses().get("coupon-table").stream()
                .map(this::mapToCouponDetail)
                .collect(Collectors.toMap(Coupons::getCouponId, Function.identity()));
        return CouponResponseDTO.builder().status(HttpStatus.OK).coupons(couponMap).build();
    }

    /**
     * Maps a DynamoDB item to a Coupon object.
     *
     * @param item The DynamoDB item.
     * @return The mapped Coupon object.
     */
    @Override

    public Coupons mapToCouponDetail(Map<String, AttributeValue> item) {
        String couponID = getStringOrNull(item, "CouponID");
        String couponType = getStringOrNull(item, "CouponType");
        String couponCode = getStringOrNull(item, "CouponCode");
        String expire = getStringOrNull(item, "Expire");
        String limit = getStringOrNull(item, "Limit");
        String totalData = getStringOrNull(item, "TotalData");
        String image = getStringOrNull(item, "Image");
        String description = getStringOrNull(item, "Description");
        String url = getStringOrNull(item, "Url");
        return Coupons.builder()
                .couponId(couponID)
                .type(couponType)
                .couponCode(couponCode)
                .expire(expire)
                .limit(limit)
                .data(totalData)
                .image(image)
                .description(description)
                .url(url)
                .build();
    }

    /**
     * Retrieves the string value of an attribute from a DynamoDB item, or returns null if the attribute doesn't exist.
     *
     * @param item The DynamoDB item.
     * @param key  The attribute key.
     * @return The string value of the attribute, or null if the attribute doesn't exist.
     */
    @Override
    public String getStringOrNull(Map<String, AttributeValue> item, String key) {
        return item.containsKey(key) ? item.get(key).s() : null;
    }

    /**
     * Retrieves coupon data by its ID.
     *
     * @param couponId   The ID of the coupon.
     * @param couponType The coupon type.
     * @return The coupon data.
     * @throws ResourceNotFoundException if the coupon is not found.
     */
    @Override
    public String getCouponDataByCouponId(String couponId, String couponType) {
        try {
            GetItemRequest getRequest = queryCouponsHelper.getCouponDataByCouponId(couponId, couponType);
            Map<String, AttributeValue> response = dynamoDbClient.getItem(getRequest).item();
            if (response.isEmpty()) {
                throw new ResourceNotFoundException("Coupon not found with id " + couponId);
            }
            return queryWrapper.mapToCouponTotalData(response);
        } catch (Exception e) {
            throw new ResourceNotFoundException("No coupon found with id: " + couponId);
        }
    }



    @Override
    public Boolean updateLimit(String couponId){
        Coupons coupon = getByCouponId(couponId);
        if(coupon.getLimit().equals("1")){
            adminService.deleteCouponById(couponId);
        }
        else {
            int limit = Integer.parseInt(coupon.getLimit());
            limit = limit - 1;
            coupon.setLimit(Integer.toString(limit));
            adminService.updateCouponById(couponId,coupon);
        }
        return true;
    }

}



