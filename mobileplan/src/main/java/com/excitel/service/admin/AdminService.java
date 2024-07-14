package com.excitel.service.admin;

import com.excitel.model.Coupons;
import com.excitel.model.MobilePlan;

import java.util.List;

public interface AdminService {
    public MobilePlan addMobilePlan(MobilePlan mobilePlan);

    public MobilePlan updateMobilePlan(String planId, MobilePlan mobilePlan);

    public String deleteMobilePlan(String planId, String planType);

    public List<Coupons> getAllCoupons();

    public Coupons getCouponById(String couponId);
    public Coupons getCouponByIdForUser(String couponId);

    public Coupons createCoupons(Coupons coupons);

    public Coupons updateCouponById(String couponId, Coupons updatedCoupon);

    public String deleteCouponById(String couponId);


}
