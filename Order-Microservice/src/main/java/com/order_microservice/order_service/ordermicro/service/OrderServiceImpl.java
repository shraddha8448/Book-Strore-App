package com.order_microservice.order_service.ordermicro.service;

import com.order_microservice.order_service.ordermicro.client.BookClient;
import com.order_microservice.order_service.ordermicro.client.UserClient;
import com.order_microservice.order_service.ordermicro.dto.OrderDTO;
import com.order_microservice.order_service.ordermicro.exception.OrderNotFoundException;
import com.order_microservice.order_service.ordermicro.external.Book;
import com.order_microservice.order_service.ordermicro.external.User;
import com.order_microservice.order_service.ordermicro.modal.Order;
import com.order_microservice.order_service.ordermicro.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookClient bookClient;

    @Autowired
    private UserClient userClient;

    public OrderDTO mapToOrderDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setAddress(order.getAddress());
        orderDTO.setQty(order.getQty());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setCancel(order.getCancel());
        orderDTO.setBookId(order.getBookId());

        return orderDTO;
    }

    @Override
    public OrderDTO orderPlaced(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setQty(orderDTO.getQty());
        order.setAddress(orderDTO.getAddress());
        order.setUserId(orderDTO.getUserId());
        order.setBookId(orderDTO.getBookId());
        order.setCancel(false);

        Book book = bookClient.aParticularBook(orderDTO.getBookId());
        bookClient.placedOrderUpdateBookTable(orderDTO.getBookId(), orderDTO.getQty());
        order.setPrice(book.getBookPrice()*orderDTO.getQty());

        return mapToOrderDTO(orderRepository.save(order));
    }

    @Override
    public OrderDTO canceledOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order != null){
            if(!order.getCancel()){
                order.setCancel(true);
                bookClient.addedBackToBook(order.getBookId(),order.getQty());
                return mapToOrderDTO(orderRepository.save(order));
            }else{
                throw new OrderNotFoundException("The order is already canceled...");
            }
        }else {
            throw new OrderNotFoundException("Order Not Found");
        }
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        List<Order> orders = new ArrayList<>();
        for (Order order:orderList){
            if(!order.getCancel())
                orders.add(order);
        }
        return orders.stream()
                .map(this::mapToOrderDTO)
                .toList();
    }

    @Override
    public List<OrderDTO> getAllOrdersForUser(String token) {
        User user = userClient.getUser(token);
        List<Order> orderList = orderRepository.findAllByUserId(user.getId());
        return orderList.stream()
                .map(this::mapToOrderDTO)
                .toList();
    }

}
