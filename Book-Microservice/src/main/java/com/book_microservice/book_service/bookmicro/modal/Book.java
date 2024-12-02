package com.book_microservice.book_service.bookmicro.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String bookName;
    private String bookAuthor;
    private String bookDescription;

    @Lob
    private byte[] bookLogo;
    private long bookPrice;
    private long bookQuantity;

}
