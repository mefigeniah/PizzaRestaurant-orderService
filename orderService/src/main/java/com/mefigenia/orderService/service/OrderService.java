package com.mefigenia.orderService.service;

import com.mefigenia.orderService.dao.InfoOrderDao;
import com.mefigenia.orderService.dao.OrderDao;
import com.mefigenia.orderService.feign.OrderInterface;
import com.mefigenia.orderService.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    InfoOrderDao infoOrderDao;

    @Autowired
    OrderInterface orderInterface;

    public ResponseEntity<String> createOrder(Integer tableNro, String serverName) {
        if(infoOrderDao.findByTableNro(tableNro).isPresent()){
            if(infoOrderDao.findByTableNro(tableNro).get().getStatus()) {
                return new ResponseEntity<>("Table is already open", HttpStatus.OK);
            }
        }

        InfoOrder infoOrder = new InfoOrder();
        infoOrder.setTableNro(tableNro);
        infoOrder.setServerName(serverName);
        infoOrder.setStatus(true);
        infoOrderDao.save(infoOrder);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    public ResponseEntity<String> addItemToOrder(Integer orderId, Integer pizzasId, ToppingObject extraToppings, char size, Integer units) {
        //Checking if the order is open
        if(infoOrderDao.findById(orderId).get().getStatus()) {
            //Creating a new object
            PizzaOrder order = new PizzaOrder();

            //Looking for the last item added to the table
            List<PizzaOrder> orders = orderDao.findByOrderId(orderId);


            //if the list is empty this is the first item to be created
            if(orders.isEmpty()) {
                order.setItemNumber(1);
            }
            else {
                //Looking for the last item added
                order.setItemNumber(orders.get(orders.size() - 1).getItemNumber() + 1);
            }


            //Getting the type of pizza the client wants
            Optional<Pizza> typeOfPizza = orderInterface.getPizza(pizzasId).getBody();


            //Checking the status of the pizza
            if (!typeOfPizza.get().getAvailability()) {
                return new ResponseEntity<>("Pizza is not available at the moment", HttpStatus.OK);
            }


            // If the pizza is available, get the toppings based on the type of pizza
            ToppingObject toppings = typeOfPizza.get().getToppings();

            //Getting the toppings from the database
            if(pizzasId == 7) {
                order.setToppings(extraToppings);
                System.out.println("Entro por ser 7");
                if(checkPizza(order, size, orders, extraToppings)) {
                    System.out.println("Es cierto");
                    return new ResponseEntity<>("Success", HttpStatus.OK);
                }
            } else {
                String[] toppingsDb = toppings.getToppings();
                String[] toppingsAmountDb = toppings.getAmount();


                //Getting the extra toppings from the user
                String[] xToppings = extraToppings.getToppings();
                String[] amountExtraToppings = extraToppings.getAmount();


                //getting the total toppings
                int toppingsDbLength = toppingsDb.length;
                int xToppingsLength = xToppings.length;
                int totalLength = toppingsDbLength + xToppingsLength;


                //Creating a new object type with its Strings[]
                // To store the total of toppings and their amounts
                String[] newToppings = new String[totalLength];
                String[] newAmount = new String[totalLength];
                ToppingObject newToppingObject = new ToppingObject();


                //Storing the toppings from the DB in the new string[]
                if (!toppingsDb[0].equals("null")) {
                    for (int i = 0; i < toppingsDb.length; ++i) {
                        newToppings[i] = toppingsDb[i];
                        newAmount[i] = toppingsAmountDb[i];
                    }
                } else toppingsDbLength = 0;

                int iteration = toppingsDbLength;


                //Storing the extra toppings in the new string[]
                if (!xToppings.toString().isEmpty()) {
                    for (int i = 0; i < xToppings.length; ++i) {
                        for (int j = 0; j < toppingsDb.length; j++) {
                            if (xToppings[i].equals(newToppings[j])) {
                                newAmount[j] = amountExtraToppings[i];
                                break;
                            } else if (j == (toppingsDb.length - 1)) {
                                newToppings[iteration] = xToppings[i];
                                newAmount[iteration] = amountExtraToppings[i];
                                ++iteration;
                            }
                        }
                    }
                }

                // Checking if the pizza already exist on the last items ordered in the same order
//                if (order.getItemNumber() != 1) {
//                    //Getting all the toppings of each item in the last orders
//                    for (PizzaOrder pOrder : orders) {
//                        ToppingObject pOrderToppings = pOrder.getToppings();
//                        String[] toppingsPOrder = pOrderToppings.getToppings();
//                        String[] toppingsAmountPOrder = pOrderToppings.getAmount();
//
//
//                        if (Arrays.equals(toppingsPOrder, newToppings)) {
//                            if (Arrays.equals(toppingsAmountPOrder, newAmount)) {
//                                if (Objects.equals(size, pOrder.getSize())) {
//                                    pOrder.setUnits(pOrder.getUnits() + 1);
//                                    orderDao.save(pOrder);
//                                    return new ResponseEntity<>("Success", HttpStatus.OK);
//                                }
//                            }
//                        }
//                    }
//                }
            //Storing values into the new Topping object
            newToppingObject.setToppings(newToppings);
            newToppingObject.setAmount(newAmount);

            if(checkPizza(order, size, orders, newToppingObject)) {
                System.out.println("calling checkPizza method");
                return new ResponseEntity<>("Success", HttpStatus.OK);
            }

            //setting the toppings into the order object
            order.setToppings(newToppingObject);
            }
            //Setting the nro of order
            order.setOrderId(orderId);

            //setting the pizza id
            order.setPizzasId(pizzasId);


            //Setting the size of the pizza
            order.setSize(size);



            //Setting units per pizza
            order.setUnits(units);

            //getting the cost per size
            double costPerSize = orderInterface.priceSize(size).getBody();

            //getting the cost per topping
            double costPerTopping = orderInterface.priceToppings(size).getBody() * order.getToppings().getToppings().length;

            //total cost
            double totalCost = costPerSize + costPerTopping;

            //Setting the cost
            order.setCost(totalCost);

            //Setting the date
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);
            order.setDate(formattedDate);

            orderDao.save(order);

        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    private boolean checkPizza(PizzaOrder order, int size, List<PizzaOrder> orders, ToppingObject toppings) {
        System.out.println("dentro del method");
        // Checking if the pizza already exist on the last items ordered in the same order
        if (order.getItemNumber() != 1) {
            System.out.println("Dentro del if");
            //Getting all the toppings of each item in the last orders
            for (PizzaOrder pOrder : orders) {
                ToppingObject pOrderToppings = pOrder.getToppings();
                String[] toppingsPOrder = pOrderToppings.getToppings();
                String[] toppingsAmountPOrder = pOrderToppings.getAmount();

                System.out.println("toppingsPOrder " + Arrays.toString(toppingsPOrder) + " toppings.getToppings() " + Arrays.toString(toppings.getToppings()));
                if (Arrays.equals(toppingsPOrder, toppings.getToppings())) {
                    System.out.println("Son  toppings");
                    if (Arrays.equals(toppingsAmountPOrder, toppings.getAmount())) {
                        System.out.println("Son iguales las amount");
                        System.out.println("Size " + size + " pOrder.getSize() " + pOrder.getSize());
                        if (size == pOrder.getSize()) {
                            System.out.println("Son iguales las sizes");
                            pOrder.setUnits(pOrder.getUnits() + 1);
                            orderDao.save(pOrder);
                            System.out.println("Guardando Dao");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ResponseEntity<String> closeOrder(Integer orderId) {
        Optional<InfoOrder> infoOrder = infoOrderDao.findById(orderId);
        infoOrder.get().setStatus(false);
        infoOrderDao.save(infoOrder.get());

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    public ResponseEntity<String> openOrder(Integer orderId) {
        Optional<InfoOrder> infoOrder = infoOrderDao.findById(orderId);
        infoOrder.get().setStatus(true);
        infoOrderDao.save(infoOrder.get());

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    public ResponseEntity<Double> orderTotal(Integer orderId) {
        List<PizzaOrder> pizzaOrders = orderDao.findByOrderId(orderId);
        double total = 0.0;

        for (PizzaOrder  pOrder : pizzaOrders) {
            total = total + (pOrder.getCost() * pOrder.getUnits());
        }

        return new ResponseEntity<>(total, HttpStatus.OK);
    }
}
