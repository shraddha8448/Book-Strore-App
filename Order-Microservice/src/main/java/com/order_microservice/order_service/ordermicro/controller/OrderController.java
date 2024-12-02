package com.order_microservice.order_service.ordermicro.controller;

import com.order_microservice.order_service.ordermicro.client.UserClient;
import com.order_microservice.order_service.ordermicro.dto.OrderDTO;
import com.order_microservice.order_service.ordermicro.exception.OrderNotFoundException;
import com.order_microservice.order_service.ordermicro.external.User;
import com.order_microservice.order_service.ordermicro.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private OrderService orderService;

    private User getAuthenticatedUser(String authHeader) {
        User user = userClient.getUser(authHeader);
        if(user!=null)
            return user;

        return null;
    }

    @PostMapping("/orderPlaced")
    public ResponseEntity<?> orderPlaced(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody OrderDTO orderDTO) {

        User isAuthenticatedUser = getAuthenticatedUser(authHeader);
        if(isAuthenticatedUser!=null) {

            if(!orderDTO.getCancel()) {
                OrderDTO dto = orderService.orderPlaced(orderDTO);
                return new ResponseEntity<>(orderDTO,HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new OrderNotFoundException("Order Canceled"),HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("unauthorised", HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/canceled/{orderId}")
    public ResponseEntity<?> orderCanceled(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable Long orderId) {
        User isAuthenticatedUser = getAuthenticatedUser(authHeader);

        if (isAuthenticatedUser != null) {
            orderService.canceledOrder(orderId);
            return new ResponseEntity<>("Order canceled successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<?> getAllOrders(@RequestHeader("Authorization") String authHeader){
        User isAuthenticatedUser = getAuthenticatedUser(authHeader);
        if(isAuthenticatedUser != null){
            List<OrderDTO> orderDTOList = orderService.getAllOrders();

            return new ResponseEntity<>(orderDTOList,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Unauthorized User", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getOrder/forUser")
    public ResponseEntity<?> getAllOrdersForUser(@RequestHeader("Authorization") String authHeader){
        User isAuthenticatedUser = getAuthenticatedUser(authHeader);
        if(isAuthenticatedUser != null){
            List<OrderDTO> orderDTOList = orderService.getAllOrdersForUser(authHeader);
            return new ResponseEntity<>(orderDTOList,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Unauthorized User", HttpStatus.UNAUTHORIZED);
        }
    }
}
