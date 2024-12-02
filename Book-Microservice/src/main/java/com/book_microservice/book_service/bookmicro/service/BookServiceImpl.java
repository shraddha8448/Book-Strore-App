package com.book_microservice.book_service.bookmicro.service;

import com.book_microservice.book_service.bookmicro.modal.Book;
import com.book_microservice.book_service.bookmicro.repository.BookRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book addBook(Book book, MultipartFile file) throws IOException {
        book.setBookLogo(file.getBytes());
        return bookRepository.save(book);
    }

    @Override
    @CircuitBreaker(name = "bookBreaker",fallbackMethod = "bookBreakerFallback")
    //@Retry(name = "bookBreaker",fallbackMethod = "bookBreakerFallback")
    //@RateLimiter(name = "bookBreaker")
    public List<Book> getAllBookDetails() {
        return bookRepository.findAll();
    }

    public List<String> bookBreakerFallback(Exception e){
        List<String> list = new ArrayList<>();
        list.add("Dummy");
        return list;
    }

    @Override
    @CircuitBreaker(name = "bookBreaker",fallbackMethod = "getBookDetailsFallback")
    public Book getBookDetailsById(Long bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public Book getBookDetailsFallback(Long bookId, Throwable throwable) {
        // Log the exception for debugging purposes
        System.err.println("Circuit breaker triggered for getBookDetailsById: " + throwable.getMessage());

        // Return a default or null value as a fallback
        Book fallbackBook = new Book();
        fallbackBook.setId(bookId);
        fallbackBook.setBookName("Default Book Name");
        fallbackBook.setBookAuthor("Unknown Author");
        fallbackBook.setBookDescription("Book details are currently unavailable.");
        fallbackBook.setBookPrice(0);
        fallbackBook.setBookQuantity(0);

        return fallbackBook;
    }


    @Override
    public Book updateBookLogoById(Long bookId, MultipartFile file) throws IOException {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isEmpty()){
            return null;
        }
        Book book = optionalBook.get();
        book.setBookLogo(file.getBytes());
        return bookRepository.save(book);
    }

    @Override
    public String deleteBookDetailsById(Long bookId) {

        Book book = bookRepository.findById(bookId).orElse(null);
        if(book != null){
            bookRepository.deleteById(bookId);
            return "Book deleted Successfully";
        }else{
            return "Book id not found";
        }
    }

    @Override
    public String ChangeBookQuantity(Long bookId, Long quantity) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isEmpty())
            return "Book not found";

        Book book = optionalBook.get();
        book.setBookQuantity(quantity);
        bookRepository.save(book);
        return "BookId : "+ bookId + "\nquantity updated successfully";
    }

    @Override
    public String ChangeBookPrice(Long bookId, Long price) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isEmpty())
            return "Book Not found";

        Book book = optionalBook.get();
        book.setBookPrice(price);
        bookRepository.save(book);
        return "Book price change SuccessFully";
    }

    @Override
    @CircuitBreaker(name = "cartBreaker", fallbackMethod = "addToCartFallback")
    public String addToCartUpdateBookQuantity(Long oldQty,Long bookId, Long newQty) {

        Book book = bookRepository.findById(bookId).orElse(null);

        if (book==null) {
            return "qty not reduced from book table";
        } else {
            Long afterAddedToCartQuantity = book.getBookQuantity()+oldQty - newQty;
            if(book.getBookQuantity()+oldQty >= newQty){
                book.setBookQuantity(afterAddedToCartQuantity);// decrease qty from book table
                bookRepository.save(book);
                return "qty updated from book table";
            }else{
                return "Available : "+ book.getBookQuantity()+oldQty+"\nRequired : "+newQty;
            }

        }
    }

    public String addToCartFallback(Long oldQty, Long bookId, Long newQty, Throwable throwable) {

        System.err.println("Circuit breaker triggered for addToCartUpdateBookQuantity: " + throwable.getMessage());
        return "Service is currently unavailable. Please try again later.";
    }


    @Override
    @CircuitBreaker(name = "bookTableBreaker", fallbackMethod = "addBackToBookTableFallback")
    public String addBackToBookTable(Long bookId,Long qty){

        Book books = bookRepository.findById(bookId).orElse(null);
        if(books!=null) {
            books.setBookQuantity(books.getBookQuantity()+qty);
            bookRepository.save(books);
            return "book data added back to book table";
        } else {
            return "book id not found";
        }
    }

    public String addBackToBookTableFallback(Long bookId, Long qty, Throwable throwable) {

        System.err.println("Circuit breaker triggered for addBackToBookTable: " + throwable.getMessage());
        return "Service is currently unavailable. Unable to add back to the book table at the moment.";
    }


    @Override
    @CircuitBreaker(name = "orderBreaker", fallbackMethod = "orderPlacedFallback")
    public String orderPlacedUpdateBookTable(Long bookId, Long qty){
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book==null) {
            return "book not available...";
        } else {
            Long afterPlacedOrder = book.getBookQuantity() - qty;
            if(afterPlacedOrder >= 0){
                book.setBookQuantity(afterPlacedOrder); //decrease qty from book table
                bookRepository.save(book);
                return "Placed order successfully...";
            }else{
                return "Available : "+ book.getBookQuantity()+"\nRequired : "+qty;
            }
        }
    }

    public String orderPlacedFallback(Long bookId, Long qty, Throwable throwable) {
        System.err.println("Circuit breaker triggered for orderPlacedUpdateBookTable: " + throwable.getMessage());
        return "Service is currently unavailable. Unable to place the order at this time. Please try again later.";
    }

}
