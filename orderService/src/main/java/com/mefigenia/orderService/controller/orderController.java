package com.mefigenia.orderService.controller;

import com.mefigenia.orderService.model.OrderDto;
import com.mefigenia.orderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class orderController {

    @Autowired
    OrderService orderService;

    @PostMapping("create")
    public  ResponseEntity<String> createOrder(@RequestParam Integer tableNro, @RequestParam String serverName) {
        return orderService.createOrder(tableNro, serverName);
    }

    @PostMapping("addItems")
    public  ResponseEntity<String> addItemToOrder(@RequestBody OrderDto order) {
        return orderService.addItemToOrder(order.getOrderId(), order.getPizzasId(), order.getExtraToppings(), order.getSize(), order.getUnits());
    }

    @PostMapping("close")
    public  ResponseEntity<String> closeOrder(@RequestParam Integer orderId) {
        return orderService.closeOrder(orderId);
    }

    @PostMapping("open")
    public  ResponseEntity<String> openOrder(@RequestParam Integer orderId) {
        return orderService.openOrder(orderId);
    }

    @GetMapping("total")
    public  ResponseEntity<Double> orderTotal (@RequestParam Integer orderId) {
        return orderService.orderTotal(orderId);
    }
}
