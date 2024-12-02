package com.cart_microservice.cart_service.cartmicro.service;

import com.cart_microservice.cart_service.cartmicro.client.BookClient;
import com.cart_microservice.cart_service.cartmicro.exception.NoSuchCartException;
import com.cart_microservice.cart_service.cartmicro.external.Book;
import com.cart_microservice.cart_service.cartmicro.modal.Cart;
import com.cart_microservice.cart_service.cartmicro.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookClient bookClient;

    @Override
    public void addToCart(Cart cart) {
        Book book = bookClient.getBookById(cart.getBookId());
        cart.setTotalPrice(book.getBookPrice()* cart.getQuantity());
        bookClient.addToCartUpdateBookQuantity(0L,cart.getBookId(), cart.getQuantity());
        cartRepository.save(cart);
    }

    @Override
    public Cart getByCartId(Long cartId){
        return cartRepository.findById(cartId).orElse(null);
    }

    @Override
    public Cart  removeFromCart(long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        try {
            if (cart != null){
                bookClient.addedBackToBook(cart.getBookId(), cart.getQuantity());
                cartRepository.deleteById(cartId);
                return cart;
            }else {
                throw new NoSuchCartException("cart Id: " +cartId +" Not found in cart list" );
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while communicating with the Book service: "+ e);
        }

    }

    @Override
    public List<Cart> deleteCartByUserId(long userId) {
        List<Cart> allByUserId = cartRepository.findByUserId(userId);
        if (allByUserId.isEmpty()){
            return null;
        }

        for (Cart cart : allByUserId) {
            bookClient.addedBackToBook(cart.getBookId(),1L);
            cartRepository.deleteById(cart.getCartId());
        }
        return allByUserId;
    }

    @Override
    public Cart updateQuantityInCartItem(Long cartId, Long qty){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null && qty > 0){
            bookClient.addToCartUpdateBookQuantity(cart.getQuantity(),cart.getBookId(),qty);
            cart.setQuantity(qty);
            Book book = bookClient.getBookById(cart.getBookId());
            cart.setTotalPrice(book.getBookPrice()*qty);
            return cartRepository.save(cart);
        }
        return null;
    }

    @Override
    public List<Cart> getAllByUserId(Long userId) {
        System.out.println(cartRepository.findByUserId(userId));
        List<Cart> cartList = cartRepository.findByUserId(userId);
        return cartList;// cartRepository.findAllByUserId(userId);
    }

    @Override
    public List<Cart> getAll() {
        return cartRepository.findAll();
    }

}