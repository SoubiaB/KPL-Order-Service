package com.kpl.order.model;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class PaymentDetails {
    @Id
    private String cardId;

    private Long cardNumber;

    private Integer cvv;

    private Date expiryDate;

    private String orderNumber;

    private double amount;
}
