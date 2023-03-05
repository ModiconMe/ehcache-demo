package edu.modicon.ehcachedemo.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerRegistrationRequest {
    private String name;
    private String email;
    private int age;
}
