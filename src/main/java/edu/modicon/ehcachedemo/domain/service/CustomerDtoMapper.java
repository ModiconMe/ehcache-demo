package edu.modicon.ehcachedemo.domain.service;

import edu.modicon.ehcachedemo.domain.model.Customer;
import edu.modicon.ehcachedemo.application.dto.CustomerDto;
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
