package com.kpl.order.model;

import lombok.Data;

@Data
public class CustomerDetails {

    private String firstName;
    private String lastName;
    private String email;
    private Long phone;
    private String pinCode;
    private String Address;
    private String city;
    private String state;
}
