package com.example.demo.controller;

import com.example.demo.controller.requests.ModifyCartRequest;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ItemRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final BigDecimal PRICE = new BigDecimal(10);
    private static final String DESCRIPTION = "description";
    private static final String APPLE = "apple";
    private static final long ITEM_ID = 1L;
    private static final int QUANTITY = 3;

    private static final int ONE_INVOCATION = 1;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartController cartController;

    private User user;
    private Item item;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setCart(new Cart());

        item = new Item();
        item.setId(ITEM_ID);
        item.setPrice(PRICE);
        item.setDescription(DESCRIPTION);
        item.setName(APPLE);
    }

    @Test
    public void verify_that_http_status_is_not_found_when_username_is_not_present_adding_to_cart() {
        when(this.userRepository.findByUsername(null)).thenReturn(null);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(null);

        ResponseEntity<Cart> cartResponseEntity = this.cartController.addToCart(modifyCartRequest);

        assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_http_status_is_not_found_when_item_doesnt_exist_adding_to_cart(){
        when(this.userRepository.findByUsername(any())).thenReturn(user);
        when(this.itemRepository.findById(any())).thenReturn(Optional.empty());

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(ITEM_ID);
        modifyCartRequest.setUsername(USERNAME);

        ResponseEntity<Cart> cartResponseEntity = this.cartController.addToCart(modifyCartRequest);

        verify(this.itemRepository, times(ONE_INVOCATION)).findById(any());

        assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_http_status_is_ok_when_item_was_added_to_cart(){
        when(this.userRepository.findByUsername(USERNAME)).thenReturn(user);
        when(this.itemRepository.findById(ITEM_ID)).thenReturn(Optional.ofNullable(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USERNAME);
        modifyCartRequest.setItemId(ITEM_ID);
        modifyCartRequest.setQuantity(QUANTITY);

        ResponseEntity<Cart> cartResponseEntity = this.cartController.addToCart(modifyCartRequest);

        verify(this.cartRepository, times(ONE_INVOCATION)).save(any());

        assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_http_status_is_not_found_when_username_is_not_present_removing_from_cart(){
        when(this.userRepository.findByUsername(null)).thenReturn(null);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(null);

        ResponseEntity<Cart> cartResponseEntity = this.cartController.removeFromCart(modifyCartRequest);

        assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_http_status_is_not_found_when_item_doesnt_exist_removing_from_cart(){
        when(this.userRepository.findByUsername(any())).thenReturn(user);
        when(this.itemRepository.findById(any())).thenReturn(Optional.empty());

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername(USERNAME);

        ResponseEntity<Cart> cartResponseEntity = this.cartController.removeFromCart(modifyCartRequest);

        verify(this.itemRepository, times(ONE_INVOCATION)).findById(any());

        assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_http_status_is_ok_when_item_was_removed_from_cart(){
        when(this.userRepository.findByUsername(USERNAME)).thenReturn(user);
        when(this.itemRepository.findById(ITEM_ID)).thenReturn(Optional.ofNullable(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USERNAME);
        modifyCartRequest.setItemId(ITEM_ID);
        modifyCartRequest.setQuantity(QUANTITY);

        ResponseEntity<Cart> cartResponseEntity = this.cartController.removeFromCart(modifyCartRequest);

        verify(this.cartRepository, times(ONE_INVOCATION)).save(any());

        assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
    }
}
