package com.mefigenia.orderService.model;

import jakarta.persistence.Convert;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PizzaWrapper {
    private int Integer;
    private String name;
    @Convert(converter = ToppingsAttributeConverter.class)
    private ToppingObject toppings;
    }
