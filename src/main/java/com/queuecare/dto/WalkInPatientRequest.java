package com.queuecare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalkInPatientRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;
}

