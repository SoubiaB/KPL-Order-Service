package com.kpl.order.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.List;

@Data
@Document(collection = "OrderDetails")
public class OrderDetails {
    @Id
    private String orderId;
    private Date orderDate;
    private List<ItemDetails> items;
    private CustomerDetails customerDetails;
    private PaymentDetails paymentDetails;
    private String paymentStatus;

}
