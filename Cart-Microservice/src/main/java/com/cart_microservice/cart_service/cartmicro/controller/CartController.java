package com.cart_microservice.cart_service.cartmicro.controller;

import com.cart_microservice.cart_service.cartmicro.client.BookClient;
import com.cart_microservice.cart_service.cartmicro.client.UserClient;
import com.cart_microservice.cart_service.cartmicro.exception.NoSuchCartException;
import com.cart_microservice.cart_service.cartmicro.external.Book;
import com.cart_microservice.cart_service.cartmicro.external.User;
import com.cart_microservice.cart_service.cartmicro.modal.Cart;
import com.cart_microservice.cart_service.cartmicro.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private BookClient bookClient;


    private User getAuthenticatedUser(String authHeader) {
        User user = userClient.getUser(authHeader);
        System.out.println(user);
        if(user!=null)
            return user;

        return null;
    }

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestHeader("Authorization") String authHeader, @RequestBody Cart cart){

        User isUserAuthenticated = getAuthenticatedUser(authHeader);
        if(isUserAuthenticated != null){
            Book book = bookClient.getBookById(cart.getBookId());
            if(book != null && book.getBookQuantity()>0){
                if(book.getBookQuantity() < cart.getQuantity()){
                    return new ResponseEntity<>("Only "+book.getBookQuantity()+" are available; "+cart.getQuantity()+" requested.",HttpStatus.CONFLICT);
                }
                cartService.addToCart(cart);
                return new ResponseEntity<>("Book added successfully in cart",HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Book Not Available",HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>("UnAuthorized User", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/removeFromCart/{cartId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartId){
        Cart cart = cartService.removeFromCart(cartId);
        if(cart != null){
            return  new ResponseEntity<>("Successfully remove from the cart",HttpStatus.OK);
        }
        throw new NoSuchCartException("No Such a Cart of cart Id :"+cartId);
    }

    @DeleteMapping("/removeByUserId")
    public ResponseEntity<?> removeByUserId(@RequestHeader("Authorization") String authHeader){
        User isUserAuthenticated = getAuthenticatedUser(authHeader);

        if(isUserAuthenticated != null){
            List<Cart> cartList = cartService.deleteCartByUserId(isUserAuthenticated.getId());
            if (cartList.isEmpty()){

                return new ResponseEntity<>(new NoSuchCartException("User does not have this cart access"),HttpStatus.NOT_FOUND);
            }else {
                return new ResponseEntity<>("Successfully Deleted cart of User Id: "+isUserAuthenticated.getId(),HttpStatus.OK);
            }

        }else {
            return new ResponseEntity<>("UnAuthorized User",HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/updateQty")
    public ResponseEntity<String> updateQuantityInCartItem(@RequestHeader("Authorization") String authHeader,
                                                           @RequestParam Long cartId,
                                                           @RequestParam Long qty){
        User isUserAuthenticated = getAuthenticatedUser(authHeader);

        if(isUserAuthenticated != null){
            Cart cart = cartService.getByCartId(cartId);
            if(cart != null){
                Book book = bookClient.getBookById(cart.getBookId());
                if(qty > book.getBookQuantity()+cart.getQuantity()){
                    return new ResponseEntity<>("Only "+book.getBookQuantity()+cart.getQuantity()+" are available",HttpStatus.CONFLICT);
                }
                cartService.updateQuantityInCartItem(cartId,qty);
                return new ResponseEntity<>("Successfully updated quantity in cart",HttpStatus.OK);
            }else{
                throw new NoSuchCartException("No Such a cart of cart Id :" + cartId);
            }
        }else{
            return new ResponseEntity<>("UnAuthorized User",HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/getCartItemsForUser")
    public List<Cart> getAllCartItemsForUser(@RequestHeader("Authorization") String authHeader){
        User isUserAuthenticated = getAuthenticatedUser(authHeader);
        if(isUserAuthenticated != null){

            System.out.println(cartService.getAllByUserId(isUserAuthenticated.getId()));
            return cartService.getAllByUserId(isUserAuthenticated.getId());
            //return new ResponseEntity<>(cartService.getAllByUserId(isUserAuthenticated.getId()),HttpStatus.OK);
        }else {
            //return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            return null;
        }
    }

    @GetMapping("/getAllCartItems")
    public ResponseEntity<List<Cart>> getAllCartItems(){
        return new ResponseEntity<>(cartService.getAll(),HttpStatus.OK);
    }
}
