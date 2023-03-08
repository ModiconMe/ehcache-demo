package edu.modicon.customer.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateRequest {

    @With
    private Long id;

    private String name;
    private String email;
    private Integer age;
}
