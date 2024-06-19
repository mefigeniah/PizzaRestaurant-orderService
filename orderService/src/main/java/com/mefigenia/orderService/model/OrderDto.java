package com.mefigenia.orderService.model;

import lombok.Data;

@Data
public class OrderDto {
    private Integer orderId;
    private Integer pizzasId;
    private ToppingObject extraToppings;
    private char size;
    private Integer units;
}
