package com.cart_microservice.cart_service.cartmicro.repository;

import com.cart_microservice.cart_service.cartmicro.modal.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    List<Cart> findByUserId(Long id);
}
