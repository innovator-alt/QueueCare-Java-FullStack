package com.queuecare.dto;

import com.queuecare.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private Role role;
}

