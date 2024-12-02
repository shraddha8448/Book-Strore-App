package com.order_microservice.order_service.ordermicro.external;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class Book {
    private long bookId;
    private String bookName;
    private String bookAuthor;
    private String bookDescription;

    @Lob
    private byte[] bookLogo;
    private long bookPrice;
    private long bookQuantity;

}
