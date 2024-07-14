package com.excitel.dto;

import com.excitel.model.MobilePlan;
import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobilePlanDTO {
    private HttpStatus status;
    private MobilePlan plan;
}
