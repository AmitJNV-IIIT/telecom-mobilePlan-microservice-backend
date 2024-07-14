package com.excitel.controller.admin;

import com.excitel.dto.CouponDTO;
import com.excitel.dto.CouponListDTO;
import com.excitel.dto.MobileDeleteResponseDTO;
import com.excitel.dto.MobilePlanDTO;

import com.excitel.exception.custom.*;
import com.excitel.model.Coupons;
import com.excitel.model.MobilePlan;
import com.excitel.service.admin.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Objects;

import static com.excitel.constant.AppConstants.ADMIN;
import static com.excitel.constant.AppConstants.ADMIN_EX_MSG;

/**
 * Controller for handling admin operations related to mobile plans and coupons.
 */
@RestController
@RequestMapping("/api/v2/mobile")
@CrossOrigin
@Tag(name = "Mobile Microserice User Controller", description = "Mobile microservice User Controller APIs")
public class AdminController {

    @Autowired//NOSONAR
    private AdminService adminService;



    /**
     * Endpoint for adding a new mobile plan.
     *
     * @param mobilePlan      The mobile plan object to be added.
     * @param bindingResult   Object that holds the validation errors.
     * @param httpServletRequest The HTTP servlet request.
     * @return                ResponseEntity containing the added mobile plan details.
     */
    @PostMapping
    public ResponseEntity<MobilePlanDTO> addMobilePlan(@Valid @RequestBody MobilePlan mobilePlan, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            String message = "field cannot be null or empty";
            throw new MobilePlanException(message);
        }
        if (Objects.isNull(mobilePlan)) {
            return ResponseEntity.badRequest().build();
        }

        String role = (String) httpServletRequest.getAttribute("role");
        if (Objects.equals(role, ADMIN.getValue())) {

            MobilePlan addedMobilePlan = adminService.addMobilePlan(mobilePlan);
            MobilePlanDTO response = MobilePlanDTO.builder()
                    .status(HttpStatus.CREATED)
                    .plan(addedMobilePlan)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            throw new UserAccessDeniedException(ADMIN_EX_MSG.getValue());
        }
    }
    /**
     * Endpoint for updating an existing mobile plan.
     *
     * @param planId            The ID of the mobile plan to be updated.
     * @param mobilePlan        The updated mobile plan object.
     * @param httpServletRequest The HTTP servlet request.
     * @return                  ResponseEntity containing the updated mobile plan details.
     */

    @PutMapping("/{planId}")
    public ResponseEntity<MobilePlanDTO> updateMobilePlan(@PathVariable String planId, @RequestBody MobilePlan mobilePlan, HttpServletRequest httpServletRequest)  {
        if (planId == null || mobilePlan == null) {
            return ResponseEntity.badRequest().build();
        }
        String role = (String) httpServletRequest.getAttribute("role");
        if (Objects.equals(role, ADMIN.getValue())) {
            MobilePlan updatedMobilePlan = adminService.updateMobilePlan(planId, mobilePlan);
            MobilePlanDTO response = MobilePlanDTO.builder()
                    .status(HttpStatus.OK)
                    .plan(updatedMobilePlan)
                    .build();
            if (updatedMobilePlan != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                throw new PlanNotFoundException("Plan not found");
            }

        } else {
            throw new UserAccessDeniedException(ADMIN_EX_MSG.getValue());
        }
    }

    /**
     * Endpoint for deleting a mobile plan by its ID.
     *
     * @param planId            The ID of the mobile plan to be deleted.
     * @param planType          The type of the mobile plan.
     * @param httpServletRequest The HTTP servlet request.
     * @return                  ResponseEntity containing the status of the deletion operation.
     */

    @DeleteMapping("/{planType}/{planId}")
    public ResponseEntity<MobileDeleteResponseDTO> deleteMobilePlan(@PathVariable String planId, @PathVariable String planType, HttpServletRequest httpServletRequest){
        if (planId == null) {
            return ResponseEntity.badRequest().build();
        }
        String role = (String) httpServletRequest.getAttribute("role");
        if (Objects.equals(role, ADMIN.getValue())) {
            String responseMessage = adminService.deleteMobilePlan(planId, planType);

            if ((planId + " Deleted successfully").equals(responseMessage)) {
                MobileDeleteResponseDTO responseDTO = MobileDeleteResponseDTO.builder()
                        .message(responseMessage)
                        .status(HttpStatus.OK)
                        .build();

                return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
            } else if ((planId + " Already deleted").equals((responseMessage))) {
                MobileDeleteResponseDTO response = MobileDeleteResponseDTO.builder().status(HttpStatus.OK).message(responseMessage).build();
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                throw new PlanNotFoundException("Plan not found with associate ID");
            }
        } else {

            throw new UserAccessDeniedException(ADMIN_EX_MSG.getValue());
        }
    }
    /**
     * Endpoint for creating a new coupon.
//     *
//     * @param coupons           The coupon object to be created.
//     * @param bindingResult     Object that holds the validation errors.
     * @param httpServletRequest The HTTP servlet request.
     * @return                  ResponseEntity containing the created coupon details.
     */


    @GetMapping("/coupons")
    public ResponseEntity<CouponListDTO> getAllCoupons(HttpServletRequest httpServletRequest){
        String role = (String) httpServletRequest.getAttribute("role");
        if (Objects.equals(role, ADMIN.getValue())) {
            List<Coupons> createdCoupon = adminService.getAllCoupons();
            CouponListDTO response = CouponListDTO.builder()
                    .status(HttpStatus.OK)
                    .coupons(createdCoupon)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } else {

            throw new UserAccessDeniedException(ADMIN_EX_MSG.getValue());
        }
    }

    @GetMapping("/coupons/{couponId}")
    public ResponseEntity<CouponDTO> getCouponById(@PathVariable String couponId,HttpServletRequest httpServletRequest){
        String role = (String) httpServletRequest.getAttribute("role");
        if (Objects.equals(role, ADMIN.getValue())) {
            Coupons couponDetail = adminService.getCouponById(couponId);
            CouponDTO response = CouponDTO.builder()
                    .status(HttpStatus.OK)
                    .coupon(couponDetail)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else if(Objects.equals(role, "USER")){
            Coupons couponDetail = adminService.getCouponByIdForUser(couponId);
            CouponDTO response = CouponDTO.builder()
                    .status(HttpStatus.OK)
                    .coupon(couponDetail)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        else {
            throw new UserAccessDeniedException(ADMIN_EX_MSG.getValue());
        }
    }

    @PostMapping("/coupons")
    public ResponseEntity<CouponDTO> createCoupon(@Valid @RequestBody Coupons coupons, BindingResult bindingResult, HttpServletRequest httpServletRequest){
        if (bindingResult.hasErrors()) {
            String message = "Field cannot be null or empty";
            throw new CouponsException(message);
        }
        if (coupons == null) {
            return ResponseEntity.badRequest().build();
        }
        String role = (String) httpServletRequest.getAttribute("role");
        if (Objects.equals(role, ADMIN.getValue())) {
            Coupons createdCoupon = adminService.createCoupons(coupons);
            CouponDTO response = CouponDTO.builder()
                    .status(HttpStatus.CREATED)
                    .coupon(createdCoupon)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } else {

            throw new UserAccessDeniedException(ADMIN_EX_MSG.getValue());
        }
    }
    /**
     * Updates a coupon by its ID.
     *
     * @param couponId          The ID of the coupon to update
     * @param updatedCoupon     The updated coupon data
     * @param httpServletRequest The HTTP servlet request
     * @return ResponseEntity containing the updated coupon
     */
    @PutMapping("/coupons/{couponId}")
    public ResponseEntity<CouponDTO> updateCouponById(@PathVariable String couponId, @RequestBody Coupons updatedCoupon, HttpServletRequest httpServletRequest){
        if (couponId == null || updatedCoupon == null) {
            return ResponseEntity.badRequest().build();
        }
        String role = (String) httpServletRequest.getAttribute("role");
        if (Objects.equals(role, ADMIN.getValue())) {
            Coupons updateCoupon = adminService.updateCouponById(couponId, updatedCoupon);
            CouponDTO updated = CouponDTO.builder()
                    .status(HttpStatus.OK)
                    .coupon(updateCoupon)
                    .build();

            if(updated != null) {

                return ResponseEntity.status(HttpStatus.OK).body(updated);
            } else {

                throw new CouponsException("Coupon not found");
            }
        } else {

            throw new UserAccessDeniedException(ADMIN_EX_MSG.getValue());
        }

    }
    /**
     * Deletes a coupon by its ID.
     *
     * @param couponId          The ID of the coupon to delete
     * @param httpServletRequest The HTTP servlet request
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity<CouponDTO> deleteCouponById(@PathVariable String couponId, HttpServletRequest httpServletRequest) {
        if (couponId == null) {
            return ResponseEntity.badRequest().build();
        }
        String role = (String) httpServletRequest.getAttribute("role");
        if (Objects.equals(role, ADMIN.getValue())) {
            String responseMessage = adminService.deleteCouponById( couponId);
            if ("Coupon deleted successfully".equals(responseMessage)) {
                CouponDTO responseDTO = CouponDTO.builder()
                        .status(HttpStatus.OK)
                        .build();

                return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
            } else {

                throw new CouponsException("Coupon not found with id: " + couponId);
            }
        } else {

            throw new UserAccessDeniedException(ADMIN_EX_MSG.getValue());
        }
    }
}
