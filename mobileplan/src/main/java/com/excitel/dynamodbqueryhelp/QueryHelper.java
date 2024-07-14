package com.excitel.dynamodbqueryhelp;

import com.excitel.model.MobilePlan;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;


@Component
public class QueryHelper {

    private static final String PLANTYPE = "PlanType";
    private static final String PLANID = "PlanID";
    private static final String TABLENAME = "plan-table";
    private static final String ACTIVE = "Active";

//    @Cacheable("plan-table")
    /**
     * Creates a GetItemRequest to retrieve a mobile plan by its ID and type.
     *
     * @param planId   The ID of the mobile plan.
     * @param planType The type of the mobile plan.
     * @return         GetItemRequest object for retrieving the mobile plan.
     */
    public GetItemRequest getItemByPlanId(String planId, String planType){
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(PLANTYPE, AttributeValue.builder().s(planType).build());
        key.put(PLANID, AttributeValue.builder().s(planId).build());

        return GetItemRequest.builder()
                .tableName(TABLENAME)
                .key(key)
                .build();
    }
    /**
     * Creates a PutItemRequest to add a new mobile plan.
     *
     * @param mobilePlan The mobile plan object to be added.
     * @return           PutItemRequest object for adding the mobile plan.
     */
    public PutItemRequest addQuery(MobilePlan mobilePlan){
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(PLANID, AttributeValue.builder().s(mobilePlan.getPlanId()).build());
        item.put(PLANTYPE, AttributeValue.builder().s(!Objects.isNull(mobilePlan.getPlanType())?mobilePlan.getPlanType():"Broadband").build());
        item.put("Price",AttributeValue.builder().s(!Objects.isNull(mobilePlan.getPrice())? mobilePlan.getPrice():"null").build());
        item.put("Category",AttributeValue.builder().s(!Objects.isNull(mobilePlan.getCategory())?mobilePlan.getCategory():"null").build());
        item.put("Validity",AttributeValue.builder().s(!Objects.isNull(mobilePlan.getValidity())?mobilePlan.getValidity():"null").build());
        List<AttributeValue> ottAttributes = mobilePlan.getOtt() != null ?
                mobilePlan.getOtt().stream()
                        .map(s -> AttributeValue.builder().s(s).build())
                        .toList() :
                Collections.emptyList();
        item.put("OTT", AttributeValue.builder().l(ottAttributes).build());
        item.put("VoiceLimit",AttributeValue.builder().s(!Objects.isNull(mobilePlan.getVoiceLimit())?mobilePlan.getVoiceLimit():"null").build());
        item.put("SMS", AttributeValue.builder().s(!Objects.isNull(mobilePlan.getSms()) ? mobilePlan.getSms() : "null").build());
        item.put("TotalData",AttributeValue.builder().s(!Objects.isNull(mobilePlan.getData())? mobilePlan.getData() : "null").build());
        List<String> couponIds = mobilePlan.getCouponIds();
        List<AttributeValue> couponAttributes = couponIds != null ?
                couponIds.stream()
                        .map(s -> AttributeValue.builder().s(s).build())
                        .toList() :
                Collections.emptyList();
        item.put("CouponIDs", AttributeValue.builder().l(couponAttributes).build());
        item.put("PlanLimit", AttributeValue.builder().s(!Objects.isNull(mobilePlan.getLimit()) ? mobilePlan.getLimit() : "null").build());
        item.put(ACTIVE, AttributeValue.builder().s(!Objects.isNull(mobilePlan.getActive()) ? mobilePlan.getActive() : "True").build());


        return PutItemRequest.builder()
                .tableName(TABLENAME)
                .conditionExpression("attribute_not_exists(PlanType) AND attribute_not_exists(PlanID)")
                .item(item)
                .build();
    }
    /**
     * Creates an UpdateItemRequest to update a mobile plan.
     *
     * @param mobilePlan The updated mobile plan object.
     * @param planId     The ID of the mobile plan to be updated.
     * @return           UpdateItemRequest object for updating the mobile plan.
     */

    public UpdateItemRequest updateQuery(MobilePlan mobilePlan, String planId){

        Map<String, AttributeValue> key = new HashMap<>();
        key.put(PLANTYPE, AttributeValue.builder().s(mobilePlan.getPlanType()).build());
        key.put(PLANID, AttributeValue.builder().s(planId).build());

        Map<String, AttributeValueUpdate> item = new HashMap<>();

        item.put("Price", AttributeValueUpdate.builder().value(AttributeValue.builder().s(mobilePlan.getPrice()).build()).build());
        item.put("Category", AttributeValueUpdate.builder().value(AttributeValue.builder().s(mobilePlan.getCategory()).build()).build());
        item.put("Validity", AttributeValueUpdate.builder().value(AttributeValue.builder().s(mobilePlan.getValidity()).build()).build());
        List<AttributeValue> ottAttributes = mobilePlan.getOtt() != null ?
                mobilePlan.getOtt().stream()
                        .map(s -> AttributeValue.builder().s(s).build())
                        .toList() :
                Collections.emptyList();
        item.put("OTT", AttributeValueUpdate.builder().value(AttributeValue.builder().l(ottAttributes).build()).build());
        item.put("VoiceLimit", AttributeValueUpdate.builder().value(AttributeValue.builder().s(mobilePlan.getVoiceLimit()).build()).build());
        item.put("SMS", AttributeValueUpdate.builder().value(AttributeValue.builder().s(mobilePlan.getSms()).build()).build());
        item.put("TotalData", AttributeValueUpdate.builder().value(AttributeValue.builder().s(mobilePlan.getData()).build()).build());
        List<String> couponIds = mobilePlan.getCouponIds();
        List<AttributeValue> couponAttributes = couponIds != null ?
                couponIds.stream()
                        .map(s -> AttributeValue.builder().s(s).build())
                        .toList() :
                Collections.emptyList();
        item.put("CouponIDs", AttributeValueUpdate.builder().value(AttributeValue.builder().l(couponAttributes).build()).build());
        item.put("PlanLimit", AttributeValueUpdate.builder().value(AttributeValue.builder().s(mobilePlan.getLimit()).build()).build());
        item.put(ACTIVE, AttributeValueUpdate.builder().value(AttributeValue.builder().s(mobilePlan.getActive()).build()).build());

        return UpdateItemRequest.builder()
                .tableName(TABLENAME)
                .key(key)
                .attributeUpdates(item)
                .build();
    }
    /**
     * Creates an UpdateItemRequest to delete a mobile plan.
     *
     * @param planId     The ID of the mobile plan to be deleted.
     * @param mobilePlan The mobile plan object to be marked as inactive.
     * @return           UpdateItemRequest object for deleting the mobile plan.
     */
    public UpdateItemRequest deleteQuery(String planId, MobilePlan mobilePlan){

        Map<String, AttributeValue> key = new HashMap<>();
        key.put(PLANTYPE, AttributeValue.builder().s(mobilePlan.getPlanType()).build());
        key.put(PLANID, AttributeValue.builder().s(planId).build());

        Map<String, AttributeValueUpdate> item = new HashMap<>();

        item.put(ACTIVE, AttributeValueUpdate.builder().value(AttributeValue.builder().s("False").build()).build());

        return UpdateItemRequest.builder()
                .tableName(TABLENAME)
                .key(key)
                .attributeUpdates(item)
                .build();
    }

}
