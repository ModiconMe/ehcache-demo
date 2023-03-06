package edu.modicon.ehcachedemo.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerRegistrationRequest {
    private String name;
    private String email;
    private int age;
}
