package com.book_microservice.book_service.bookmicro.service;

import com.book_microservice.book_service.bookmicro.modal.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {

    Book addBook(Book book, MultipartFile file) throws IOException;

    List<Book> getAllBookDetails();
    Book getBookDetailsById(Long id);
    String deleteBookDetailsById(Long id);
    Book updateBookLogoById(Long id,MultipartFile file) throws IOException;

    String ChangeBookQuantity(Long bookid,Long quantity);
    String ChangeBookPrice(Long bookid,Long price);

    String addToCartUpdateBookQuantity(Long oldQty,Long bookId, Long qty);
    String addBackToBookTable(Long bookId,Long qty);

    String orderPlacedUpdateBookTable(Long bookId, Long qty);

}
