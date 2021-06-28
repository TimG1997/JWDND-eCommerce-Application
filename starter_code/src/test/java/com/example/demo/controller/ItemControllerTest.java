package com.example.demo.controller;

import com.example.demo.entity.Item;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    private static final long ITEM_ID = 0L;
    private static final long ITEM_ONE_ID = 1L;
    private static final long ITEM_TWO_ID = 2L;
    private static final long ITEM_THREE_ID = 3L;
    private static final long ITEM_FOUR_ID = 4L;
    private static final String APPLE = "apple";
    private static final String BREAD = "bread";

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    private List<Item> allItems;
    private List<Item> appleItems;
    private Item item;

    @BeforeEach
    public void beforeEach(){
        this.appleItems = new ArrayList<>();
        this.allItems = new ArrayList<>();

        Item itemOne = new Item();
        itemOne.setId(ITEM_ONE_ID);
        itemOne.setName(APPLE);

        Item itemTwo = new Item();
        itemTwo.setId(ITEM_TWO_ID);
        itemTwo.setName(APPLE);

        Item itemThree = new Item();
        itemThree.setId(ITEM_THREE_ID);
        itemThree.setName(APPLE);

        Item itemFour = new Item();
        itemFour.setId(ITEM_FOUR_ID);
        itemFour.setName(BREAD);

        this.appleItems.addAll(Arrays.asList(itemOne, itemTwo, itemThree));
        this.allItems.addAll(Arrays.asList(itemOne, itemTwo, itemThree, itemFour));

        item = new Item();
        item.setId(ITEM_ID);
        item.setName(APPLE);
    }

    @Test
    public void verify_that_items_can_be_retrieved(){
        when(this.itemRepository.findAll()).thenReturn(this.allItems);

        ResponseEntity<List<Item>> itemsResponseEntity = this.itemController.getItems();

        assertEquals(HttpStatus.OK, itemsResponseEntity.getStatusCode());
        assertEquals(4, itemsResponseEntity.getBody().size());
    }

    @Test
    public void verify_that_item_can_be_retrieved_by_id(){
        when(this.itemRepository.findById(ITEM_ID)).thenReturn(Optional.ofNullable(item));

        ResponseEntity<Item> itemResponseEntity = this.itemController.getItemById(ITEM_ID);

        assertEquals(HttpStatus.OK, itemResponseEntity.getStatusCode());
        assertNotNull(itemResponseEntity.getBody());
        assertEquals(ITEM_ID, itemResponseEntity.getBody().getId());
    }

    @Test
    public void verify_that_http_status_is_not_found_when_there_are_no_items_available(){
        when(this.itemRepository.findByName(APPLE)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Item>> itemsResponseEntity = this.itemController.getItemsByName(APPLE);

        assertEquals(HttpStatus.NOT_FOUND, itemsResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_http_status_is_not_found_when_there_items_are_null(){
        when(this.itemRepository.findByName(APPLE)).thenReturn(null);

        ResponseEntity<List<Item>> itemsResponseEntity = this.itemController.getItemsByName(APPLE);

        assertEquals(HttpStatus.NOT_FOUND, itemsResponseEntity.getStatusCode());
    }

    @Test
    public void verify_that_items_can_be_retrieved_by_name(){
        when(this.itemRepository.findByName(APPLE)).thenReturn(appleItems);

        ResponseEntity<List<Item>> itemsResponseEntity = this.itemController.getItemsByName(APPLE);

        assertEquals(HttpStatus.OK, itemsResponseEntity.getStatusCode());
        assertNotNull(itemsResponseEntity.getBody());
        assertEquals(3, itemsResponseEntity.getBody().size());
    }

}
