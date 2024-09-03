package com.kpl.order.service;

import com.kpl.order.model.OrderDetails;
import com.kpl.order.model.PaymentDetails;
import com.kpl.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    final String paymentService = "http://localhost:8081/payment";
    private static final String topic = "orders";

    public ResponseEntity<String> saveOrder(OrderDetails orderDetails) {
        PaymentDetails payment = orderDetails.getPaymentDetails();

        String orderId = orderDetails.getOrderId();
        payment.setOrderNumber(orderId);
        log.info("payment is {}", payment);

        HttpEntity<PaymentDetails> request = new HttpEntity<>(payment);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(paymentService, HttpMethod.POST, request, String.class);
            log.info("status is {} {}", response, response.getStatusCode());
        }catch (Exception e){
            log.error("Payment Failed");
            response=new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }



        if (response.getStatusCode().equals(HttpStatus.OK) || response.getStatusCode().equals(HttpStatus.CREATED)) {
            orderDetails.setPaymentStatus("PrePaid");
        }else{
            orderDetails.setPaymentStatus("COD");
        }


        try {
            OrderDetails savedOrder = orderRepository.save(orderDetails);
            kafkaTemplate.send(topic, savedOrder.toString());

            return ResponseEntity.ok(savedOrder.toString());
        } catch (Exception e) {
            log.error("catch block " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<OrderDetails> getById(String id) {
        try {
            orderRepository.existsById(id);
            return ResponseEntity.ok(orderRepository.findById(id).get());

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    public List<OrderDetails> getAllOrders() {
        return orderRepository.findAll();
    }

    public ResponseEntity<OrderDetails> updateOrderDetails(OrderDetails orderDetails) {
        log.info("Incoming Order to update is {}", orderDetails);
        try{
            String orderId = orderDetails.getOrderId();
            if(orderId.isEmpty()){
                return ResponseEntity.notFound().build();
            }


            if(orderRepository.existsById(orderId)){
                OrderDetails orderFromDatabase=orderRepository.findById(orderId).get();
                String paymentStatus=orderFromDatabase.getPaymentStatus();
                orderDetails.setPaymentStatus(paymentStatus);

                return ResponseEntity.ok(orderRepository.save(orderDetails));
            }else{
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    public String deleteOrderById(String id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return "OrderDetails Successfully deleted";
        }
        return "Deletion failed";
    }

    public String deleteAll() {
        try {
            orderRepository.deleteAll();
            return "Deleted Order details Successfully";
        } catch (Exception e) {
            return "Failed to delete";
        }
    }
}
