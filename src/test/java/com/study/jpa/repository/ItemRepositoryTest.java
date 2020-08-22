package com.study.jpa.repository;

import com.study.jpa.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    private  ItemRepository itemRepository;

    @DisplayName("엔티티가 generatedvalue 사용할 경우 save 테스트")
    @Test
    public void saveTest(){
        Item item = new Item();

        itemRepository.save(item);
    }

    @DisplayName("엔티티의 식별자를 직접 사용할 경우 save 테스트")
    @Test
    public void saveTestForPersistable(){
        Item item = new Item("A");

        itemRepository.save(item);
    }



}