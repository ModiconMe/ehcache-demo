package edu.modicon.ehcachedemo.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CustomerRegistrationRequest {
    private String name;
    private String email;
    private int age;
}
