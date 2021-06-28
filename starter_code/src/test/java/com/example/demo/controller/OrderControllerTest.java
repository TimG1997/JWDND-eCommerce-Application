package com.example.demo.controller;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.entity.UserOrder;
import com.example.demo.repository.UserOrderRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String APPLE = "apple";
    private static final String DESCRIPTION = "description";
    private static final BigDecimal PRICE = new BigDecimal(10);

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserOrderRepository userOrderRepository;

    @InjectMocks
    private OrderController orderController;

    private User user;
    private List<UserOrder> orders;

    @BeforeEach
    public void beforeEach(){
        Cart cart = new Cart();

        Item itemOne = new Item();
        itemOne.setName(APPLE);
        itemOne.setDescription(DESCRIPTION);
        itemOne.setPrice(PRICE);

        Item itemTwo = new Item();
        itemTwo.setName(APPLE);
        itemTwo.setDescription(DESCRIPTION);
        itemTwo.setPrice(PRICE);

        cart.addItem(itemOne);
        cart.addItem(itemTwo);

        user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setCart(cart);

        this.orders = new ArrayList<>();
        this.orders.add(new UserOrder());
        this.orders.add(new UserOrder());
        this.orders.add(new UserOrder());
    }

    @Test
    public void verify_that_http_status_is_not_found_when_user_doesnt_exist(){
        when(this.userRepository.findByUsername(any())).thenReturn(null);

        ResponseEntity<UserOrder> userOrderResponseEntity = this.orderController.submit(USERNAME);

        assertEquals(HttpStatus.NOT_FOUND, userOrderResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_user_order_is_returned_when_order_is_submitted(){
        when(this.userRepository.findByUsername(USERNAME)).thenReturn(user);

        ResponseEntity<UserOrder> userOrderResponseEntity = this.orderController.submit(USERNAME);

        assertEquals(HttpStatus.OK, userOrderResponseEntity.getStatusCode());
        assertEquals(2, userOrderResponseEntity.getBody().getItems().size());
    }

    @Test
    public void verify_that_http_status_is_not_found_when_user_doesnt_exist_calling_getting_orders(){
        when(this.userRepository.findByUsername(any())).thenReturn(null);

        ResponseEntity<List<UserOrder>> userOrderResponseEntity = this.orderController.getOrdersForUser(USERNAME);

        assertEquals(HttpStatus.NOT_FOUND, userOrderResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_you_can_retrieve_the_order_history_of_a_user(){
        when(this.userRepository.findByUsername(USERNAME)).thenReturn(user);
        when(this.userOrderRepository.findByUser(user)).thenReturn(this.orders);

        ResponseEntity<List<UserOrder>> ordersForUserResponseEntity = this.orderController.getOrdersForUser(USERNAME);

        assertEquals(HttpStatus.OK, ordersForUserResponseEntity.getStatusCode());
        assertEquals(3, ordersForUserResponseEntity.getBody().size());
    }
}
