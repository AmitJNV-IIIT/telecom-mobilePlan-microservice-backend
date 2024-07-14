package com.excitel.service.user;

import com.excitel.dto.CouponResponseDTO;
import com.excitel.dto.RequestDTO;
import com.excitel.model.Coupons;
import com.excitel.model.MobilePlan;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import java.util.List;
import java.util.Map;


public interface MobilePlanService {

    public List<MobilePlan> getMobilePlanWithQuery(RequestDTO params);
    public Coupons getByCouponId(String couponId);
    public CouponResponseDTO getAllCouponByCouponIdList(List<String> couponIds,String couponType);
    public Coupons mapToCouponDetail(Map<String, AttributeValue> item);

    public String getStringOrNull(Map<String, AttributeValue> item, String key);
    public String getCouponDataByCouponId(String couponId,String couponType);

    public Boolean updateLimit(String couponId);

}
