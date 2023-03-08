package edu.modicon.customer.domain.service;

import edu.modicon.customer.domain.model.Customer;
import edu.modicon.customer.application.dto.CustomerDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CustomerDtoMapper implements Function<Customer, CustomerDto> {

    @Override
    public CustomerDto apply(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .age(customer.getAge())
                .build();
    }
}
