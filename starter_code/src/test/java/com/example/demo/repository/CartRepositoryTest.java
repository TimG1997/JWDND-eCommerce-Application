package com.example.demo.repository;

import com.example.demo.entity.Cart;
import com.example.demo.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

@DataJpaTest
@DirtiesContext
public class CartRepositoryTest {

    private static final String USER = "user";
    private static final String PASSWORD = "password";

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void beforeClass(){
        user = new User();
        user.setUsername(USER);
        user.setPassword(PASSWORD);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotal(new BigDecimal(10));

        user.setCart(cart);

        user = this.userRepository.save(user);
    }

    @Test
    public void verify_that_a_cart_can_be_retrieved_for_a_user(){
        Cart cartByUser = this.cartRepository.findByUser(user);

        Assertions.assertEquals(new BigDecimal(10).doubleValue(), cartByUser.getTotal().doubleValue());
    }
}
