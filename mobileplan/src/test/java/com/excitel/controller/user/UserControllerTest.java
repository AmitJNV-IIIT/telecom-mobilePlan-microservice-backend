package com.excitel.controller.user;
import static org.mockito.Mockito.when;

import com.excitel.dto.CouponRequestDTO;
import com.excitel.dto.CouponResponseDTO;
import com.excitel.dto.RequestDTO;
import com.excitel.service.user.MobilePlanService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserControllerTest {
    @MockBean
    private MobilePlanService mobilePlanService;

    @Autowired
    private UserController userController;

    /**
     * Method under test: {@link UserController#getAllMobilePlans(RequestDTO)}
     */
    @Test
    void testGetAllMobilePlans() throws Exception {
        // Arrange
        when(mobilePlanService.getMobilePlanWithQuery(Mockito.<RequestDTO>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/mobile");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"OK\",\"data\":[]}"));
    }

    /**
     * Method under test:
     * {@link UserController#getCouponByCouponId(CouponRequestDTO)}
     */
    @Test
    void testGetCouponByCouponId() throws Exception {
        // Arrange
        CouponResponseDTO.CouponResponseDTOBuilder builderResult = CouponResponseDTO.builder();
        CouponResponseDTO buildResult = builderResult.coupons(new HashMap<>()).status(HttpStatus.CONTINUE).build();
        when(mobilePlanService.getAllCouponByCouponIdList(Mockito.<List<String>>any(), Mockito.<String>any()))
                .thenReturn(buildResult);

        CouponRequestDTO couponRequestDTO = new CouponRequestDTO();
        couponRequestDTO.setCouponIdList(new ArrayList<>());
        couponRequestDTO.setCouponType("Coupon Type");
        String content = (new ObjectMapper()).writeValueAsString(couponRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v2/mobile/coupon-details")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"CONTINUE\",\"coupons\":{}}"));
    }

    /**
     * Method under test: {@link UserController#getCouponDataByCouponID(String)}
     */
    @Test
    void testGetCouponDataByCouponID() throws Exception {
        // Arrange
        when(mobilePlanService.getCouponDataByCouponId(Mockito.<String>any(), Mockito.<String>any())).thenReturn("42");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/mobile/coupon/data/{couponId}",
                "42");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"OK\",\"couponData\":\"42\"}"));
    }

    /**
     * Method under test: {@link UserController#initalisation()}
     */
    @Test
    void testInitalisation() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/mobile/health");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("live"));
    }

    /**
     * Method under test: {@link UserController#initalisation()}
     */
    @Test
    void testInitalisation2() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/mobile/health");
        requestBuilder.contentType("https://example.org/example");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("live"));
    }

    /**
     * Method under test: {@link UserController#updateLimitByCouponId(String)}
     */
    @Test
    void testUpdateLimitByCouponId() throws Exception {
        // Arrange
        when(mobilePlanService.updateLimit(Mockito.<String>any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/mobile/coupon-limit/{couponId}",
                "42");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"OK\",\"coupons\":null}"));
    }
}