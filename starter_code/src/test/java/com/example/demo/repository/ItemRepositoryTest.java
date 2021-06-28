package com.example.demo.repository;

import com.example.demo.entity.Item;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext
public class ItemRepositoryTest {

    private static final String APPLE = "apple";
    private static final String BREAD = "bread";
    private static final String DESCRIPTION = "description";
    private static final BigDecimal PRICE = new BigDecimal(10);

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void beforeEach(){
        Item itemOne = new Item();
        itemOne.setName(APPLE);
        itemOne.setDescription(DESCRIPTION);
        itemOne.setPrice(PRICE);

        Item itemTwo = new Item();
        itemTwo.setName(APPLE);
        itemTwo.setDescription(DESCRIPTION);
        itemTwo.setPrice(PRICE);

        Item itemThree = new Item();
        itemThree.setName(BREAD);
        itemThree.setDescription(DESCRIPTION);
        itemThree.setPrice(PRICE);

        this.itemRepository.saveAll(Arrays.asList(itemOne, itemTwo, itemThree));
    }

    @Test
    public void verify_that_an_item_can_be_retrieved_by_name(){
        List<Item> items = this.itemRepository.findByName(APPLE);

        assertEquals(2, items.size());
        assertEquals(APPLE, items.get(0).getName());
        assertEquals(APPLE, items.get(1).getName());
    }
}
