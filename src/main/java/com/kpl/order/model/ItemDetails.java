package com.kpl.order.model;

import lombok.Data;

@Data
public class ItemDetails {
    private String itemName;
    private int quantity;
    private int itemPrice;

}
