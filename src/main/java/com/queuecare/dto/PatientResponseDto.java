package com.queuecare.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatientResponseDto {

    private Long id;
    private String name;
    private String phone;
    private String email;
}

