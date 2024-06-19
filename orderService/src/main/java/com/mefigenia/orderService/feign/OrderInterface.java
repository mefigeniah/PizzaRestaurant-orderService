package com.mefigenia.orderService.feign;

import com.mefigenia.orderService.model.Pizza;
import com.mefigenia.orderService.model.PizzaWrapper;
import com.mefigenia.orderService.model.Size;
import com.mefigenia.orderService.model.Toppings;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient("RESTAURANTPIZZA")
public interface OrderInterface {

    @GetMapping("toppings/all")
    public ResponseEntity<List<Toppings>> allToppings();

    @GetMapping("size/all")
    public ResponseEntity<List<Size>> allSizes();

    @GetMapping("size/priceSize")
    public ResponseEntity<Double> priceSize(@RequestParam char size);


    @GetMapping("size/priceToppings")
    public ResponseEntity<Double> priceToppings(@RequestParam char size);

    @GetMapping("Menu/pizzas")
    public ResponseEntity<List<PizzaWrapper>> allPizzas();


    @GetMapping("Menu/pizza")
    public ResponseEntity<Optional<Pizza>> getPizza(@RequestParam Integer pizzaId);
}
