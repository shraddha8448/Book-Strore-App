package com.cart_microservice.cart_service.cartmicro.service;

import com.cart_microservice.cart_service.cartmicro.modal.Cart;

import java.util.List;

public interface CartService {

    void addToCart(Cart cart);

    Cart  removeFromCart(long cartId);

    Cart getByCartId(Long cartId);

    List<Cart> deleteCartByUserId(long userId);

    Cart updateQuantityInCartItem(Long cartId, Long qty);

    List<Cart> getAllByUserId(Long userId);

    List<Cart> getAll();

}
