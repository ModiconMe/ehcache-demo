package edu.modicon.ehcachedemo.application.dto;

import lombok.Builder;

@Builder
public record CustomerDto(
        Long id,
        String name,
        String email,
        int age
) {
}
