package com.excitel.dto;


import com.excitel.model.MobilePlan;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobilePlanListDTO {
    private HttpStatus status;
    private List<MobilePlan> data;
}
