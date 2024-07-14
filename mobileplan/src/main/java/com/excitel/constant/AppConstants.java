package com.excitel.constant;

public enum AppConstants {
    ADMIN("ADMIN"),
    ADMIN_EX_MSG("Only for admins"),
    NO_COUPON_EX_MSG("No coupon found with id: "),
    NO_PLAN_FOUND_EX_MSG("No plan found with id: "),
    INTERNAL("Internal"),
    DYNAMODB_ERROR("Error occurred while connecting to DynamoDB "),
    COUPONS_TYPE("CouponType"),
    EXPIRE("Expire"),
    IMAGE("Image"),
    DESCRIPTION("Description");
    private final String value;

    AppConstants(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }

}
