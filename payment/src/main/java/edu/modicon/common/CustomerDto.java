package edu.modicon.common;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record CustomerDto (
        Long id,
        String name,
        String email,
        int age
) implements Serializable {
}
