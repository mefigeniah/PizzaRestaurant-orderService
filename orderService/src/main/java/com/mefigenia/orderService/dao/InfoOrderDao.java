package com.mefigenia.orderService.dao;

import com.mefigenia.orderService.model.InfoOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InfoOrderDao extends JpaRepository<InfoOrder, Integer> {
    Optional<InfoOrder> findByTableNro(int tableNro);

}
