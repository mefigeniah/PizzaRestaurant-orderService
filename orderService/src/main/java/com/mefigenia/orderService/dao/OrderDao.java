package com.mefigenia.orderService.dao;

import com.mefigenia.orderService.model.PizzaOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<PizzaOrder, Integer> {

    List<PizzaOrder> findByOrderId(int orderId);
}
