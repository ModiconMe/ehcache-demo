package edu.modicon.customer.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CustomersRegistrationRequest {
    private List<CustomerRegistrationRequest> customers;
}
