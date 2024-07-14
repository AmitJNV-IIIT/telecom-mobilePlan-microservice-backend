package com.excitel.redishelper;

import com.excitel.dto.RequestDTO;
import com.excitel.model.MobilePlan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class MobilePlanRedis {
    @Autowired //NOSONAR
    private final RedisTemplate<String, Object> redisTemplate;

    public MobilePlanRedis(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addMobilePlansCache(String key, List<MobilePlan> mobilePlans) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedMobilePlans = objectMapper.writeValueAsString(mobilePlans);
            redisTemplate.opsForValue().set(key, serializedMobilePlans);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<MobilePlan> getMobilePlansCache(String key) {
        Object cachedObject = redisTemplate.opsForValue().get(key);
        if (cachedObject instanceof String string) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(string, new TypeReference<List<MobilePlan>>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        //return null
        return Collections.emptyList();
    }

    public void invalidateMobilePlanCache() {
        Set<String> keys = redisTemplate.keys("MobilePlans_*");
        if (keys != null) {
            for (String key : keys) {
                redisTemplate.delete(key);
            }
        }
    }

    public String generateKeyFromParams(RequestDTO params) {
        StringBuilder sb = new StringBuilder();
        sb.append("MobilePlans_")
                .append(params.getActive()).append("_")
                .append(params.getPlanId()).append("_")
                .append(params.getType()).append("_")
                .append(params.getCategory()).append("_")
                .append(params.getData()).append("_")
                .append(params.getDays()).append("_")
                .append(params.getOffset()).append("_")
                .append(params.getLimit());
        return sb.toString();
    }
}
