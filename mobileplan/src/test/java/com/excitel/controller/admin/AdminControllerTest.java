package com.excitel.controller.admin;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.excitel.dto.CouponDTO;
import com.excitel.dto.CouponListDTO;
import com.excitel.dto.MobileDeleteResponseDTO;
import com.excitel.dto.MobilePlanDTO;
import com.excitel.exception.custom.CouponsException;
import com.excitel.exception.custom.MobilePlanException;
import com.excitel.exception.custom.PlanNotFoundException;
import com.excitel.exception.custom.UserAccessDeniedException;
import com.excitel.middleware.MobilePlanMiddleware;
import com.excitel.model.Coupons;
import com.excitel.model.MobilePlan;
import com.excitel.service.admin.AdminService;
import com.excitel.serviceimpl.admin.AdminServiceImpl;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminServiceImpl adminService;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private MobilePlanMiddleware mobilePlanMiddleware;

    @Mock
    private HttpServletRequest httpServletRequest;

    /**
     * Method under test:
     * {@link AdminController#addMobilePlan(MobilePlan, BindingResult, HttpServletRequest)}
     */
    @Test
    void testAddMobilePlan() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();
        MobilePlan mobilePlan = new MobilePlan();
        BindException bindingResult = new BindException("Target", "Object Name");

        // Act and Assert
        assertThrows(UserAccessDeniedException.class,
                () -> adminController.addMobilePlan(mobilePlan, bindingResult, new MockHttpServletRequest()));
    }

    /**
     * Method under test:
     * {@link AdminController#addMobilePlan(MobilePlan, BindingResult, HttpServletRequest)}
     */
    @Test
    void testAddMobilePlan2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();
        BindException bindingResult = new BindException("Target", "Object Name");

        // Act
        ResponseEntity<MobilePlanDTO> actualAddMobilePlanResult = adminController.addMobilePlan(null, bindingResult,
                new MockHttpServletRequest());

        // Assert
        assertNull(actualAddMobilePlanResult.getBody());
        assertEquals(400, actualAddMobilePlanResult.getStatusCodeValue());
        assertTrue(actualAddMobilePlanResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link AdminController#addMobilePlan(MobilePlan, BindingResult, HttpServletRequest)}
     */
    @Test
    void testAddMobilePlan3() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();
        MobilePlan mobilePlan = mock(MobilePlan.class);

        BindException bindingResult = new BindException("Target", "Object Name");
        bindingResult.addError(new ObjectError("role", "role"));

        // Act and Assert
        assertThrows(MobilePlanException.class,
                () -> adminController.addMobilePlan(mobilePlan, bindingResult, new MockHttpServletRequest()));
    }

    /**
     * Method under test:
     * {@link AdminController#updateMobilePlan(String, MobilePlan, HttpServletRequest)}
     */
    @Test
    void testUpdateMobilePlan() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();
        MobilePlan mobilePlan = new MobilePlan();

        // Act and Assert
        assertThrows(UserAccessDeniedException.class,
                () -> adminController.updateMobilePlan("42", mobilePlan, new MockHttpServletRequest()));
    }

    /**
     * Method under test:
     * {@link AdminController#updateMobilePlan(String, MobilePlan, HttpServletRequest)}
     */
    @Test
    void testUpdateMobilePlan2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();

        // Act
        ResponseEntity<MobilePlanDTO> actualUpdateMobilePlanResult = adminController.updateMobilePlan(null, null,
                new MockHttpServletRequest());

        // Assert
        assertNull(actualUpdateMobilePlanResult.getBody());
        assertEquals(400, actualUpdateMobilePlanResult.getStatusCodeValue());
        assertTrue(actualUpdateMobilePlanResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link AdminController#updateMobilePlan(String, MobilePlan, HttpServletRequest)}
     */
    @Test
    void testUpdateMobilePlan3() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();

        // Act
        ResponseEntity<MobilePlanDTO> actualUpdateMobilePlanResult = adminController.updateMobilePlan("foo", null,
                new MockHttpServletRequest());

        // Assert
        assertNull(actualUpdateMobilePlanResult.getBody());
        assertEquals(400, actualUpdateMobilePlanResult.getStatusCodeValue());
        assertTrue(actualUpdateMobilePlanResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link AdminController#deleteMobilePlan(String, String, HttpServletRequest)}
     */
    @Test
    void testDeleteMobilePlan() throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();

        // Act and Assert
        assertThrows(UserAccessDeniedException.class,
                () -> adminController.deleteMobilePlan("42", "Plan Type", new MockHttpServletRequest()));
    }

    @Test
    void getCouponById_AdminRole() {
        String couponId = "123";
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        Coupons coupon = new Coupons();
        when(adminService.getCouponById(anyString())).thenReturn(coupon);

        ResponseEntity<CouponDTO> result = adminController.getCouponById(couponId, httpServletRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(coupon, result.getBody().getCoupon());
    }

    @Test
    void getCouponById_UserRole() {
        String couponId = "123";
        when(httpServletRequest.getAttribute("role")).thenReturn("USER");
        Coupons coupon = new Coupons();
        when(adminService.getCouponByIdForUser(anyString())).thenReturn(coupon);

        ResponseEntity<CouponDTO> result = adminController.getCouponById(couponId, httpServletRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(coupon, result.getBody().getCoupon());
    }

    @Test
    void getCouponById_InvalidRole() {
        String couponId = "123";
        when(httpServletRequest.getAttribute("role")).thenReturn("INVALID");

        assertThrows(UserAccessDeniedException.class, () -> adminController.getCouponById(couponId, httpServletRequest));
    }

    @Test
    void getCouponById_AdminRole_NoCoupon() {
        String couponId = "123";
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.getCouponById(anyString())).thenReturn(null);

        ResponseEntity<CouponDTO> result = adminController.getCouponById(couponId, httpServletRequest);

        assertNull(result.getBody().getCoupon());
    }


    /**
     * Method under test:
     * {@link AdminController#deleteMobilePlan(String, String, HttpServletRequest)}
     */
    @Test
    void testDeleteMobilePlan2() throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();

        // Act
        ResponseEntity<MobileDeleteResponseDTO> actualDeleteMobilePlanResult = adminController.deleteMobilePlan(null,
                "Plan Type", new MockHttpServletRequest());

        // Assert
        assertNull(actualDeleteMobilePlanResult.getBody());
        assertEquals(400, actualDeleteMobilePlanResult.getStatusCodeValue());
        assertTrue(actualDeleteMobilePlanResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link AdminController#createCoupon(Coupons, BindingResult, HttpServletRequest)}
     */
    @Test
    void testCreateCoupon() throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();
        Coupons coupons = new Coupons("42", "Data", "Expire", "Type", "Limit", "Coupon Code",
                "The characteristics of someone or something", "https://example.org/example", "Image");

        BindException bindingResult = new BindException("Target", "Object Name");

        // Act and Assert
        assertThrows(UserAccessDeniedException.class,
                () -> adminController.createCoupon(coupons, bindingResult, new MockHttpServletRequest()));
    }

    /**
     * Method under test:
     * {@link AdminController#createCoupon(Coupons, BindingResult, HttpServletRequest)}
     */
    @Test
    void testCreateCoupon2() throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();
        BindException bindingResult = new BindException("Target", "Object Name");

        // Act
        ResponseEntity<CouponDTO> actualCreateCouponResult = adminController.createCoupon(null, bindingResult,
                new MockHttpServletRequest());

        // Assert
        assertNull(actualCreateCouponResult.getBody());
        assertEquals(400, actualCreateCouponResult.getStatusCodeValue());
        assertTrue(actualCreateCouponResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link AdminController#createCoupon(Coupons, BindingResult, HttpServletRequest)}
     */
    @Test
    void testCreateCoupon3() throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();
        Coupons coupons = mock(Coupons.class);

        BindException bindingResult = new BindException("Target", "Object Name");
        bindingResult.addError(new ObjectError("role", "role"));

        // Act and Assert
        assertThrows(CouponsException.class,
                () -> adminController.createCoupon(coupons, bindingResult, new MockHttpServletRequest()));
    }

    /**
     * Method under test:
     * {@link AdminController#updateCouponById(String, Coupons, HttpServletRequest)}
     */
    @Test
    void testUpdateCouponById() throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();
        Coupons updatedCoupon = new Coupons("42", "Data", "Expire", "Type", "Limit", "Coupon Code",
                "The characteristics of someone or something", "https://example.org/example", "Image");

        // Act and Assert
        assertThrows(UserAccessDeniedException.class,
                () -> adminController.updateCouponById("42", updatedCoupon, new MockHttpServletRequest()));
    }

    /**
     * Method under test:
     * {@link AdminController#updateCouponById(String, Coupons, HttpServletRequest)}
     */
    @Test
    void testUpdateCouponById2() throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();

        // Act
        ResponseEntity<CouponDTO> actualUpdateCouponByIdResult = adminController.updateCouponById(null, null,
                new MockHttpServletRequest());

        // Assert
        assertNull(actualUpdateCouponByIdResult.getBody());
        assertEquals(400, actualUpdateCouponByIdResult.getStatusCodeValue());
        assertTrue(actualUpdateCouponByIdResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link AdminController#updateCouponById(String, Coupons, HttpServletRequest)}
     */
    @Test
    void testUpdateCouponById3() throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();

        // Act
        ResponseEntity<CouponDTO> actualUpdateCouponByIdResult = adminController.updateCouponById("foo", null,
                new MockHttpServletRequest());

        // Assert
        assertNull(actualUpdateCouponByIdResult.getBody());
        assertEquals(400, actualUpdateCouponByIdResult.getStatusCodeValue());
        assertTrue(actualUpdateCouponByIdResult.getHeaders().isEmpty());
    }


    @Test
    void updateCouponById_AdminRole() {
        String couponId = "123";
        Coupons updatedCoupon = new Coupons();
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.updateCouponById(anyString(), any(Coupons.class))).thenReturn(updatedCoupon);

        ResponseEntity<CouponDTO> result = adminController.updateCouponById(couponId, updatedCoupon, httpServletRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedCoupon, result.getBody().getCoupon());
    }

    /**
     * Method under test:
     * {@link AdminController#deleteCouponById(String, HttpServletRequest)}
     */
    @Test
    void testDeleteCouponById() throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();

        // Act and Assert
        assertThrows(UserAccessDeniedException.class,
                () -> adminController.deleteCouponById("42", new MockHttpServletRequest()));
    }

    /**
     * Method under test:
     * {@link AdminController#deleteCouponById(String, HttpServletRequest)}
     */
    @Test
    void testDeleteCouponById2() throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AdminController adminController = new AdminController();

        // Act
        ResponseEntity<CouponDTO> actualDeleteCouponByIdResult = adminController.deleteCouponById(null,
                new MockHttpServletRequest());

        // Assert
        assertNull(actualDeleteCouponByIdResult.getBody());
        assertEquals(400, actualDeleteCouponByIdResult.getStatusCodeValue());
        assertTrue(actualDeleteCouponByIdResult.getHeaders().isEmpty());
    }
    @Test
    void testAddMobilePlan_Unauthorized() {
        AdminController adminController = new AdminController();
        MobilePlan mobilePlan = new MobilePlan();
        BindException bindingResult = new BindException("Target", "Object Name");

        assertThrows(UserAccessDeniedException.class,
                () -> adminController.addMobilePlan(mobilePlan, bindingResult, new MockHttpServletRequest()));
    }
    @Test
    void testAddMobilePlan_AdminRole_Success() {
        // Prepare
        MobilePlan inputMobilePlan = getMobilePlan();
        when(adminService.addMobilePlan(any(MobilePlan.class))).thenReturn(inputMobilePlan);
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        BindException bindingResult = new BindException(inputMobilePlan, "mobilePlan");

        // Execute
        ResponseEntity<MobilePlanDTO> responseEntity = adminController.addMobilePlan(inputMobilePlan, bindingResult, httpServletRequest);

        // Verify
        verifyResponse(responseEntity, inputMobilePlan);
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

    private void verifyResponse(ResponseEntity<MobilePlanDTO> responseEntity, MobilePlan addedMobilePlan) {
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        MobilePlanDTO response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals(addedMobilePlan, response.getPlan());
    }


    @Test
    void testUpdateMobilePlan_AdminRole_Success() {
        // Given
        String planId = "123";
        MobilePlan mobilePlan = new MobilePlan();
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.updateMobilePlan(any(String.class), any(MobilePlan.class))).thenReturn(mobilePlan);

        // When
        ResponseEntity<MobilePlanDTO> response = adminController.updateMobilePlan(planId, mobilePlan, httpServletRequest);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mobilePlan, response.getBody().getPlan());
    }


    @Test
    void testUpdateMobilePlan_AdminRole_PlanNotFound() {
        // Given
        String planId = "123";
        MobilePlan mobilePlan = new MobilePlan();
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.updateMobilePlan(any(String.class), any(MobilePlan.class))).thenReturn(null);

        // When & Then
        assertThrows(PlanNotFoundException.class, () -> {
            adminController.updateMobilePlan(planId, mobilePlan, httpServletRequest);
        });
    }

    @Test
    void testDeleteMobilePlan_AdminRole_Success() throws Exception {
        // Given
        String planId = "123";
        String planType = "Prepaid";
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.deleteMobilePlan(planId, planType)).thenReturn(planId + " Deleted successfully");

        // When
        ResponseEntity<MobileDeleteResponseDTO> response = adminController.deleteMobilePlan(planId, planType, httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(planId + " Deleted successfully", response.getBody().getMessage());
    }

    @Test
    void testDeleteMobilePlan_AdminRole_AlreadyDeleted() throws Exception {
        // Given
        String planId = "123";
        String planType = "Prepaid";
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.deleteMobilePlan(planId, planType)).thenReturn(planId + " Already deleted");

        // When
        ResponseEntity<MobileDeleteResponseDTO> response = adminController.deleteMobilePlan(planId, planType, httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(planId + " Already deleted", response.getBody().getMessage());
    }

    @Test
    void testDeleteMobilePlan_AdminRole_PlanNotFound() {
        // Given
        String planId = "123";
        String planType = "Prepaid";
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.deleteMobilePlan(planId, planType)).thenReturn("Plan not found");

        // When & Then
        assertThrows(PlanNotFoundException.class, () -> {
            adminController.deleteMobilePlan(planId, planType, httpServletRequest);
        });
    }

    @Test
    void testDeleteMobilePlan_NonAdminRole() {
        // Given
        String planId = "123";
        String planType = "Prepaid";
        when(httpServletRequest.getAttribute("role")).thenReturn("USER");

        // When & Then
        assertThrows(UserAccessDeniedException.class, () -> {
            adminController.deleteMobilePlan(planId, planType, httpServletRequest);
        });
    }


    @Test
    void testCreateCoupon_AdminRole_Success() throws Exception {
        // Given
        Coupons coupons = new Coupons(); // Replace with actual implementation if needed
        BindException bindingResult = new BindException(coupons, "coupons");
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.createCoupons(any(Coupons.class))).thenReturn(coupons);

        // When
        ResponseEntity<CouponDTO> response = adminController.createCoupon(coupons, bindingResult, httpServletRequest);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(coupons, response.getBody().getCoupon());
    }

    @Test
    void testDeleteCouponById_AdminRole_Success() throws Exception {
        // Given
        String couponId = "123";
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.deleteCouponById( couponId)).thenReturn("Coupon deleted successfully");

        // When
        ResponseEntity<CouponDTO> response = adminController.deleteCouponById(couponId, httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteCouponById_AdminRole_NotFound() {
        // Given
        String couponId = "123";
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.deleteCouponById( couponId)).thenReturn("Coupon not found");

        // When & Then
        assertThrows(CouponsException.class, () -> {
            adminController.deleteCouponById(couponId, httpServletRequest);
        });
    }

    @Test
    void testGetAllCoupons_AdminRole_Success() {
        // Given
        when(httpServletRequest.getAttribute("role")).thenReturn("ADMIN");
        when(adminService.getAllCoupons()).thenReturn(new ArrayList<>());

        // When
        ResponseEntity<CouponListDTO> responseEntity = adminController.getAllCoupons(httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    /**
     * Method under test:
     * {@link AdminController#getAllCoupons(HttpServletRequest)}
     */
    @Test
    void testGetAllCoupons_NonAdminRole_ThrowsException() {
        // Given
        when(httpServletRequest.getAttribute("role")).thenReturn("USER");

        // When & Then
        assertThrows(UserAccessDeniedException.class, () -> {
            adminController.getAllCoupons(httpServletRequest);
        });
    }


}
