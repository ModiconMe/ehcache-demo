package edu.modicon.ehcachedemo.web.dto;

import lombok.Builder;

@Builder
public record CustomerDto(
        Long id,
        String name,
        String email,
        int age
) {
}
