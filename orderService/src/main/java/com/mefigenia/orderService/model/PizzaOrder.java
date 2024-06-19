package com.mefigenia.orderService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
@Entity
public class PizzaOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer orderId;
    private Integer itemNumber;
    private Integer pizzasId;
    @Convert(converter = ToppingsAttributeConverter.class)
    private ToppingObject toppings;
    private char size;
    private Integer units;
    private double cost;
    private String date;
}
