package com.excitel.dynamodbqueryhelp;

import com.excitel.model.Coupons;
import com.excitel.model.CustomerCoupon;
import com.excitel.model.MobilePlan;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.excitel.constant.AppConstants.*;


@Component
public class QueryWrapper {

    private static final String TOTAL_DATA ="TotalData";
    private static final String COUPON_ID= "CouponID";


    /**
     * Maps a DynamoDB item to a MobilePlan object.
     *
     * @param item The DynamoDB item to be mapped.
     * @return     The mapped MobilePlan object.
     */
    public MobilePlan mapTomobilePlan(Map<String, AttributeValue> item) {
        MobilePlan mobilePlan = new MobilePlan();
        mobilePlan.setPlanType(item.containsKey("PlanType") ? item.get("PlanType").s() : null);
        mobilePlan.setPlanId(item.containsKey("PlanID") ? item.get("PlanID").s() : null);
        mobilePlan.setActive(item.containsKey("Active") ? item.get("Active").s() : null);
        mobilePlan.setPrice(item.containsKey("Price") ? item.get("Price").s() : null);
        mobilePlan.setCategory(item.containsKey("Category") ? item.get("Category").s() : null);
        mobilePlan.setValidity(item.containsKey("Validity") ? item.get("Validity").s() : null);
        if (item.containsKey("OTT")) {
            List<String> ottList = item.get("OTT").l().stream()
                    .map(AttributeValue::s)
                    .toList();
            mobilePlan.setOtt(ottList);
        } else {
            mobilePlan.setOtt(null);
        }
        mobilePlan.setVoiceLimit(item.containsKey("VoiceLimit") ? item.get("VoiceLimit").s() : null);
        mobilePlan.setSms(item.containsKey("SMS") ? item.get("SMS").s() : null);
        mobilePlan.setData(item.containsKey(TOTAL_DATA) ? item.get(TOTAL_DATA).s() : null);
        if (item.containsKey("CouponIDs")) {
            List<String> couponList = item.get("CouponIDs").l().stream()
                    .map(AttributeValue::s)
                    .toList();
            mobilePlan.setCouponIds(couponList);
        } else {
            mobilePlan.setCouponIds(null);
        }
        mobilePlan.setLimit(item.containsKey("PlanLimit") ? item.get("PlanLimit").s() : null);
        mobilePlan.setSpeed(item.containsKey("Speed") ? item.get("Speed").s() : null);
        return mobilePlan;
    }
    /**
     * Maps a DynamoDB item to a Coupons object.
     *
     * @param item The DynamoDB item to be mapped.
     * @return     The mapped Coupons object.
     */

    public Coupons mapToCoupons(Map<String, AttributeValue> item){

        Coupons coupon = new Coupons();
        coupon.setCouponId(item.containsKey(COUPON_ID)? item.get(COUPON_ID).s():null );
        coupon.setType(item.containsKey( COUPONS_TYPE.getValue())? item.get(COUPONS_TYPE.getValue()).s():null );
        coupon.setCouponCode(item.containsKey("CouponCode")? item.get("CouponCode").s():null );
        coupon.setExpire(item.containsKey(EXPIRE.getValue())? item.get(EXPIRE.getValue()).s():null );
        coupon.setImage(item.containsKey(IMAGE.getValue())? item.get(IMAGE.getValue()).s():null );
        coupon.setData(item.containsKey(TOTAL_DATA)? item.get(TOTAL_DATA).s():null );
        coupon.setLimit(item.containsKey("Limit")?item.get("Limit").s():null);
        coupon.setDescription(item.containsKey(DESCRIPTION.getValue())?item.get(DESCRIPTION.getValue()).s():"Not defined");

        return coupon;
    }
    public Coupons mapToCouponsForUser(Map<String, AttributeValue> item){

        Coupons coupon = new Coupons();
        coupon.setCouponId(item.containsKey(COUPON_ID)? item.get(COUPON_ID).s():null );
        coupon.setType(item.containsKey(COUPONS_TYPE.getValue())? item.get(COUPONS_TYPE.getValue()).s():null );
        coupon.setExpire(item.containsKey(EXPIRE.getValue())? item.get(EXPIRE.getValue()).s():null );
        coupon.setImage(item.containsKey(IMAGE.getValue())? item.get(IMAGE.getValue()).s():null );
        coupon.setData(item.containsKey(TOTAL_DATA)? item.get(TOTAL_DATA).s():null );
        coupon.setDescription(item.containsKey(DESCRIPTION.getValue())?item.get(DESCRIPTION.getValue()).s():"Not defined");

        return coupon;
    }
    /**
     * Maps a list of DynamoDB items to a list of CustomerCoupon objects.
     *
     * @param items The list of DynamoDB items to be mapped.
     * @return      The mapped list of CustomerCoupon objects.
     */

        public List<CustomerCoupon> mapToCustomerCoupon(List<Map<String, AttributeValue>> items){
            List<CustomerCoupon> customerCoupons = new ArrayList<>();
            for (Map<String, AttributeValue> item : items) {
                CustomerCoupon customerCoupon = new CustomerCoupon();
                customerCoupon.setMobileNumber(item.containsKey("MobileNumber") ? item.get("MobileNumber").s() : null);
                customerCoupon.setCouponID(item.containsKey(COUPON_ID) ? item.get(COUPON_ID).s() : null);
                customerCoupon.setCouponStatus(item.containsKey("CouponStatus") ? item.get("CouponStatus").s() : null);
                customerCoupon.setActivationDate(item.containsKey("ActivationDate") ? item.get("ActivationDate").s() : null);
                customerCoupon.setCustomerCouponId(item.containsKey("CustomerCouponID") ? item.get("CustomerCouponID").s() : null);
                customerCoupons.add(customerCoupon);
            }
            return customerCoupons;
        }
    /**
     * Maps a DynamoDB item to a total data string for a coupon.
     *
     * @param item The DynamoDB item to be mapped.
     * @return     The total data string for the coupon.
     */
    public String mapToCouponTotalData(Map<String, AttributeValue> item) {
        return item.containsKey(TOTAL_DATA)? item.get(TOTAL_DATA).s():null;
    }

}
