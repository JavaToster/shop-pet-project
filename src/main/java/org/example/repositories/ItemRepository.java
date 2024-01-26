package org.example.repositories;

import org.example.models.Category;
import org.example.models.Item;
import org.example.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByShop(Shop shop);

    List<Item> findByCategory(Category category);

    @Query(value = "select * from item where lower(item_name) like lower(:itemName)", nativeQuery = true)
    List<Item> findByItemNameLike(@Param("itemName") String itemName);
}
