package com.excitel.redishelper;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.excitel.dto.RequestDTO;

import java.util.*;

import com.excitel.model.MobilePlan;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.thirdparty.jackson.core.JsonProcessingException;

@ContextConfiguration(classes = {MobilePlanRedis.class, RedisTemplate.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class MobilePlanRedisTest {
    @Autowired
    private MobilePlanRedis mobilePlanRedis;
    @MockBean
    private RedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;




    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    /**
     * Method under test: {@link MobilePlanRedis#addMobilePlansCache(String, List)}
     */


    @Test
    void testAddMobilePlansCache1() throws Exception {
        MobilePlan plan1 = new MobilePlan(); // initialize with some values
        MobilePlan plan2 = new MobilePlan(); // initialize with some values
        List<MobilePlan> mobilePlans = Arrays.asList(plan1, plan2);
        String key = "someKey";
        ObjectMapper objectMapper = new ObjectMapper();
        String serializedMobilePlans = objectMapper.writeValueAsString(mobilePlans);
        mobilePlanRedis.addMobilePlansCache(key, mobilePlans);
        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).set(key, serializedMobilePlans);
    }

            @Test
            void testAddMobilePlansCache_JsonProcessingException() throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
                // Arrange
                //TODO

//                MobilePlan plan1 = new MobilePlan(); // initialize with some values
//                MobilePlan plan2 = new MobilePlan(); // initialize with some values
//                List<MobilePlan> mobilePlans = Arrays.asList(plan1, plan2);
//                String key = "someKey";
//                ObjectMapper objectMapper = mock(ObjectMapper.class);
//                when(objectMapper.writeValueAsString(mobilePlans)).thenThrow(JsonProcessingException.class);
//
//                // Mocking
//                when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//                doThrow(JsonProcessingException.class).when(valueOperations).set(anyString(), anyString());
//
//                // Act & Assert
//                assertThrows(MobilePlanException.class, () -> {
//                    mobilePlanRedis.addMobilePlansCache(key, mobilePlans);
//                });
            }

    @Test
    void testGenerateKeyFromParams() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        // Arrange
        MobilePlanRedis mobilePlanRedis = new MobilePlanRedis(new RedisTemplate<>());
        // Act and Assert
        assertEquals("MobilePlans_null_null_Prepaid_null_null_null_0_10",
                mobilePlanRedis.generateKeyFromParams(new RequestDTO()));
    }
    /**
     * Method under test: {@link MobilePlanRedis#generateKeyFromParams(RequestDTO)}
     */
    @Test
    void generateKeyFromParams_Success()  {

        MobilePlanRedis mobilePlanRedis = new MobilePlanRedis(new RedisTemplate<>());
        RequestDTO params = mock(RequestDTO.class);
        when(params.getLimit()).thenReturn(1);
        when(params.getOffset()).thenReturn(2);
        when(params.getActive()).thenReturn("Active");
        when(params.getCategory()).thenReturn("Category");
        when(params.getData()).thenReturn("Data");
        when(params.getDays()).thenReturn("Days");
        when(params.getPlanId()).thenReturn("42");
        when(params.getType()).thenReturn("Type");
        // Act
        String actualGenerateKeyFromParamsResult = mobilePlanRedis.generateKeyFromParams(params);
        // Assert
        verify(params).getActive();
        verify(params).getCategory();
        verify(params).getData();
        verify(params).getDays();
        verify(params).getLimit();
        verify(params).getOffset();
        verify(params).getPlanId();
        verify(params).getType();
        assertEquals("MobilePlans_Active_42_Type_Category_Data_Days_2_1", actualGenerateKeyFromParamsResult);
    }
    @Test
    void testGetMobilePlansCache_ValidJson() {
        // Arrange
        String key = "valid_key";
        List<MobilePlan> expectedPlans = new ArrayList<>();
        // Populate expectedPlans with some MobilePlan objects

        String json = ""; // JSON representation of expectedPlans
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(expectedPlans);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        when(redisTemplate.opsForValue().get(key)).thenReturn(json);

        // Act
        List<MobilePlan> actualPlans = mobilePlanRedis.getMobilePlansCache(key);

        // Assert
        assertNotNull(actualPlans);
        assertEquals(expectedPlans.size(), actualPlans.size());
        // Add more assertions to compare each MobilePlan object in expectedPlans and actualPlans
    }
        @Test
        void testGetMobilePlansCache_InvalidJson() {
            // Arrange
            String key = "invalid_key";
            String invalidJson = "invalid_json_data";

            when(redisTemplate.opsForValue().get(key)).thenReturn(invalidJson);

            // Act
            List<MobilePlan> actualPlans = mobilePlanRedis.getMobilePlansCache(key);

            // Assert
            assertTrue(actualPlans.isEmpty());
            // Optionally, you can verify logging or exception handling behavior if applicable
        }
        @Test
        void testGetMobilePlansCache_NullValue() {
            // Arrange
            String key = "null_key";

            when(redisTemplate.opsForValue().get(key)).thenReturn(null);

            // Act
            List<MobilePlan> actualPlans = mobilePlanRedis.getMobilePlansCache(key);

            // Assert
            assertTrue(actualPlans.isEmpty());
        }

            @Test
            void testInvalidateMobilePlanCache_KeysExist() {
                // Arrange
                String key1 = "MobilePlans_1";
                String key2 = "MobilePlans_2";
                Set<String> keys = new HashSet<>();
                keys.add(key1);
                keys.add(key2);

                when(redisTemplate.keys("MobilePlans_*")).thenReturn(keys);

                // Act
                mobilePlanRedis.invalidateMobilePlanCache();

                // Assert
                verify(redisTemplate, times(1)).delete(key1);
                verify(redisTemplate, times(1)).delete(key2);
            }
            @Test
            void testInvalidateMobilePlanCache_NoKeys() {
                // Arrange
                Set<String> keys = new HashSet<>();

                when(redisTemplate.keys("MobilePlans_*")).thenReturn(keys);

                // Act
                mobilePlanRedis.invalidateMobilePlanCache();

                // Assert
                verify(redisTemplate, never()).delete(anyString());
            }

}
 