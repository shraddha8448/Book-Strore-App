package com.book_microservice.book_service.bookmicro.external;

import lombok.Data;
import java.time.LocalDate;

@Data
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private LocalDate registeredDate;
    private LocalDate updatedDate;
    private String email;
    private String password;
    private String role;

}
