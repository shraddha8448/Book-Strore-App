package com.book_microservice.book_service.bookmicro.controller;

import com.book_microservice.book_service.bookmicro.client.UserClient;
import com.book_microservice.book_service.bookmicro.external.User;
import com.book_microservice.book_service.bookmicro.modal.Book;
import com.book_microservice.book_service.bookmicro.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserClient userClient;

    private User getAuthenticatedAdminUser(String authHeader) {
        User user = userClient.getUser(authHeader);
        if(user!=null && user.getRole().equals("ADMIN"))
            return user;

        return null;
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBookWithLogoWithToken(@RequestHeader("Authorization") String authHeader,
                                                      @RequestPart("book") String bookJson,
                                                      @RequestPart("bookLogo") MultipartFile file) {
        try {
            User isAdmin = getAuthenticatedAdminUser(authHeader);
            if (isAdmin != null) {
                // Convert JSON string to Book object
                ObjectMapper objectMapper = new ObjectMapper();
                Book book = objectMapper.readValue(bookJson, Book.class);
                book.setBookLogo(file.getBytes());
                Book addedBook = bookService.addBook(book, file);
                return new ResponseEntity<>(addedBook, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookService.getAllBookDetails();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {

        Book book = bookService.getBookDetailsById(id);
        if(book!=null) {
            return book;
        }
        else {
            //return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBookById(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {

        User isAdmin = getAuthenticatedAdminUser(authHeader);

        if(isAdmin!=null) {
            String s = bookService.deleteBookDetailsById(id);
            return new ResponseEntity<>(s,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping(value="/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBookLogoById(@RequestHeader("Authorization") String authHeader, @RequestPart("bookLogo") MultipartFile file,
                                                @PathVariable Long id) {
        try {
            User isAdmin=getAuthenticatedAdminUser(authHeader);

            if(isAdmin!=null ) {
                if (file.isEmpty()) {
                    return new ResponseEntity<>("File is empty, please upload a valid image.", HttpStatus.BAD_REQUEST);
                }

                Book books = bookService.updateBookLogoById(id, file);
                if (books != null) {
                    return new ResponseEntity<>(books, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Book ID not found.", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update/qty/{id}")
    public ResponseEntity<String> changeBookQty(@RequestHeader("Authorization") String authHeader, @PathVariable long id,@RequestParam("qty") long qty) {

        User isAdmin=getAuthenticatedAdminUser(authHeader);
        if(isAdmin!=null) {

            String s = bookService.ChangeBookQuantity(id, qty);
            if(s!=null) {
                return new ResponseEntity<>(s,HttpStatus.OK);
            } else {
                return new ResponseEntity<>("book id not found!!",HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update/price/{id}")
    public ResponseEntity<String> changeBookPrice(@RequestHeader("Authorization") String authHeader, @PathVariable long id,@RequestParam("price") long price) {

        User isAdmin=getAuthenticatedAdminUser(authHeader);
        if(isAdmin!=null ) {
            String s = bookService.ChangeBookPrice(id, price);
            if(s!=null) {

                return new ResponseEntity<>(s,HttpStatus.OK);
            } else {
                return new ResponseEntity<>("book id not found!!",HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/decreaseBookQty/{bookId}/{oldQty}/{newQty}")
    public String addToCartDecreaseBookQuantity(@PathVariable Long oldQty,@PathVariable Long bookId,@PathVariable Long newQty) {

        return bookService.addToCartUpdateBookQuantity(oldQty,bookId,newQty);
    }

    @PutMapping("/rmvFromCartAddToBook/{bookId}/{qty}")
    public ResponseEntity<String> addedBackToBook(@PathVariable Long bookId,@PathVariable Long qty) {
        String result = bookService.addBackToBookTable(bookId,qty);
       // if (result.equals("Quantity added back to book table")) {
            return ResponseEntity.ok(result);  // 200 OK
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);  // 404 Not Found
//        }
    }

    @PutMapping("/orderPlacedUpdateBookTable/{bookId}/{qty}")
    public String placedOrderUpdateBookTable(@PathVariable Long bookId,@PathVariable Long qty){
        return bookService.orderPlacedUpdateBookTable(bookId,qty);
    }
}
