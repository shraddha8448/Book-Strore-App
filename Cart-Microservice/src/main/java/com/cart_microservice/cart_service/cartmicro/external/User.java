package com.cart_microservice.cart_service.cartmicro.external;

import lombok.Data;
import java.time.LocalDate;

@Data
public class User
{
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private LocalDate registeredDate;
    private LocalDate updatedDate;
    private String email;
    private String role;
}