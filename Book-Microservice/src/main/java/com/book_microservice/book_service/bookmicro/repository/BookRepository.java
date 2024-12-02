package com.book_microservice.book_service.bookmicro.repository;

import com.book_microservice.book_service.bookmicro.modal.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByBookName(String book);
}
