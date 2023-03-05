package edu.modicon.ehcachedemo.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class CustomersRegistrationRequest {
    private List<CustomerRegistrationRequest> customers;
}
