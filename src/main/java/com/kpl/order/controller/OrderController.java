package com.kpl.order.controller;

import com.kpl.order.model.OrderDetails;
import com.kpl.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDetails orderDetails)
    {
       return orderService.saveOrder(orderDetails);

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<OrderDetails> getOrderDetailsById(@PathVariable String id)
    {
       return orderService.getById(id);
    }

    @GetMapping("/getAll")
    public List<OrderDetails> getAllOrders()
    {
        return orderService.getAllOrders();
    }

    @PutMapping("/update")
    public ResponseEntity<OrderDetails> updateOrderDetails(@RequestBody OrderDetails orderDetails)
    {
      return orderService.updateOrderDetails(orderDetails);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrderDetailsById(@PathVariable String id)
    {
        return orderService.deleteOrderById(id);
    }

    @DeleteMapping("/deleteAll")
    public String deleteAll()
    {
        return orderService.deleteAll();
    }

}
