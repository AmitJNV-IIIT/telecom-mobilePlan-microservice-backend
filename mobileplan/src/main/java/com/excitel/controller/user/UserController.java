package com.excitel.controller.user;

import com.excitel.dto.*;
import com.excitel.service.user.MobilePlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Controller for handling user operations related to mobile plans and coupons.
 */
@RestController
@RequestMapping("/api/v2/mobile")
@CrossOrigin
@Tag(name = "Mobile User Controller", description = "Mobile User Controller APIs")
public class UserController {
    @Autowired//NOSONAR
    private MobilePlanService mobilePlanService;


    /**
     * Endpoint for checking the health of the mobile service.
     *
     * @return A string indicating the service is live.
     */
    @GetMapping("/health")
    public String initalisation() {
        return "live";
    }

    /**
     * Endpoint for retrieving all mobile plans based on query parameters.
     *
     * @param params The request parameters for filtering mobile plans.
     * @return       ResponseEntity containing the list of mobile plans.
     */
    @GetMapping
    public ResponseEntity<MobilePlanListDTO> getAllMobilePlans(@ModelAttribute RequestDTO params) {

        MobilePlanListDTO response = MobilePlanListDTO.builder()
                .status(HttpStatus.OK)
                .data(mobilePlanService.getMobilePlanWithQuery(params))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    /**
     * Endpoint for retrieving coupon details by coupon ID list.
     *
     * @param request The request object containing the list of coupon IDs.
     * @return        ResponseEntity containing the coupon details.
     */

    @PostMapping("/coupon-details")
    public ResponseEntity<CouponResponseDTO> getCouponByCouponId(@RequestBody CouponRequestDTO request) {
        CouponResponseDTO response = mobilePlanService.getAllCouponByCouponIdList(request.getCouponIdList(), "Internal");
        return ResponseEntity.ok(response);
    }
    /**
     * Endpoint for retrieving coupon data by coupon ID.
     *
     * @param couponId The ID of the coupon.
     * @return         ResponseEntity containing the coupon data.
     */

    @GetMapping("/coupon/data/{couponId}")
    public ResponseEntity<CouponDataResonseDTO> getCouponDataByCouponID(@PathVariable String couponId){
        String totalData=mobilePlanService.getCouponDataByCouponId(couponId,"Internal");
        CouponDataResonseDTO dataResponseDTO = CouponDataResonseDTO.builder()
                .status(HttpStatus.OK)
                .couponData(totalData)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponseDTO);
    }

    @GetMapping("/coupon-limit/{couponId}")
    public ResponseEntity<CouponResponseDTO> updateLimitByCouponId(@PathVariable String couponId){
        mobilePlanService.updateLimit(couponId);
        CouponResponseDTO dataResponseDTO = CouponResponseDTO.builder()
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponseDTO);
    }
}
