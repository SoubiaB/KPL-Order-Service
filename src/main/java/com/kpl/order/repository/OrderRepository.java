package com.kpl.order.repository;

import com.kpl.order.model.OrderDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderDetails, String> {
}
