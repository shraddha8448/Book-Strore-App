package com.order_microservice.order_service.ordermicro.service;

import com.order_microservice.order_service.ordermicro.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO orderPlaced(OrderDTO orderDTO);
    OrderDTO canceledOrder(Long orderId);
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getAllOrdersForUser(String token);
}
