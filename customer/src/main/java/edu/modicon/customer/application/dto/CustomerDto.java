package edu.modicon.customer.application.dto;

import lombok.Builder;

@Builder
public record CustomerDto(
        Long id,
        String name,
        String email,
        int age
) {
}
