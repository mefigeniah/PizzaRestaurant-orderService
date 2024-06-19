package com.mefigenia.orderService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data

public class Toppings {

    private int Integer;
    private String name;
    private Boolean availability;
}
