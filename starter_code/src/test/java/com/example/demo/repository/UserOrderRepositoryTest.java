package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.UserOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserOrderRepositoryTest {

    private static final BigDecimal TOTAL_ONE = new BigDecimal(10);
    private static final BigDecimal TOTAL_TWO = new BigDecimal(20);
    private static final BigDecimal TOTAL_THREE = new BigDecimal(30);

    private static final String PASSWORD = "password";
    private static final String USER_ONE = "user";
    private static final String USER_TWO = "user two";

    @Autowired
    private UserOrderRepository userOrderRepository;

    @Autowired
    private UserRepository userRepository;

    private User userOne;

    @BeforeEach
    public void beforeEach(){
        userOne = new User();
        userOne.setUsername(USER_ONE);
        userOne.setPassword(PASSWORD);

        User savedUser = userRepository.save(userOne);

        UserOrder userOrderOne = new UserOrder();
        userOrderOne.setTotal(TOTAL_ONE);
        userOrderOne.setUser(savedUser);

        UserOrder userOrderTwo = new UserOrder();
        userOrderTwo.setTotal(TOTAL_TWO);
        userOrderTwo.setUser(savedUser);

        User userTwo = new User();
        userTwo.setUsername(USER_TWO);
        userTwo.setPassword(PASSWORD);

        User savedUserTwo = this.userRepository.save(userTwo);

        UserOrder userOrderThree = new UserOrder();
        userOrderThree.setTotal(TOTAL_THREE);
        userOrderThree.setUser(savedUserTwo);

        this.userOrderRepository.saveAll(Arrays.asList(userOrderOne, userOrderTwo, userOrderThree));
    }

    @Test
    public void verify_that_user_orders_can_be_retrieved_by_user(){
        List<UserOrder> userOrders = this.userOrderRepository.findByUser(userOne);

        assertEquals(2, userOrders.size());
        assertEquals(TOTAL_ONE, userOrders.get(0).getTotal());
        assertEquals(TOTAL_TWO, userOrders.get(1).getTotal());
    }
}
