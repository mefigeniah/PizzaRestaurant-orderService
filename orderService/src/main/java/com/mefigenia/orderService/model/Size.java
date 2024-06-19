package com.mefigenia.orderService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data

public class Size {

    private Integer id;
    private char size;
    private double pricePerSize;
    private double pricePerTopping;
    private Boolean availability;
}
