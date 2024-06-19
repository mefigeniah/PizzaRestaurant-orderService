package com.mefigenia.orderService.model;

import jakarta.persistence.*;
import lombok.Data;

@Data

public class Pizza {
    private Integer pizzaId;
    private String name;
    @Convert(converter = ToppingsAttributeConverter.class)
    private ToppingObject toppings;
    private Boolean availability;
}
