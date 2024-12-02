package com.order_microservice.order_service.ordermicro.repository;

import com.order_microservice.order_service.ordermicro.modal.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findAllByUserId(Long userId);
}
