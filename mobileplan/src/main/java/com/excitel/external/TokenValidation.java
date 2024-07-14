package com.excitel.external;


import com.excitel.dto.ResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;


@FeignClient(name = "AUTH-SERVICE", url = "${base-url-stage}")
public interface TokenValidation {
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @GetMapping("/auth/check-token")
    public ResponseEntity<ResponseDTO> isValid(@RequestHeader("Authorization") String token);
}