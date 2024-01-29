package org.example.services;

import org.example.models.Category;
import org.example.models.Item;
import org.example.models.Shop;
import org.example.repositories.CategoryRepository;
import org.example.repositories.ItemRepository;
import org.example.repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private ShopRepository shopRepository;
    private CategoryRepository categoryRepository;

    private List<Item> itemsCash = new ArrayList<>();

    @Autowired
    public void setItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Autowired
    public void setShopRepository(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Item> findAllItems(){
        return itemRepository.findAll();
    }

    @Transactional
    public void add(int shopId, int itemId){
        Shop shop = shopRepository.findById(shopId).orElse(null);

        Item item = itemsCash.get(itemId);
        item.setShop(shop);
        shop.getItems().add(item);

        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<Item> findItemByShop(int id){
        return itemRepository.findByShop(shopRepository.findById(id).orElse(null));
    }

    public int addToCashStep1(Item item){
        itemsCash.add(item);
        return itemsCash.indexOf(item);
    }

    public void addToCashStep2(int id, Category category){
        Item item = itemsCash.get(id);
        item.setCategory(category);

        itemsCash.set(id, item);
    }

    @Transactional(readOnly = true)
    public List<Item> findByCategory(int id){
        return itemRepository.findByCategory(categoryRepository.findById(id).orElse(null));
    }

    @Transactional(readOnly = true)
    public List<Item> findItemLikeSearchText(String text){
        return itemRepository.findByItemNameLike("%"+text+"%");
    }

    @Transactional(readOnly = true)
    public Item findById(int id){
        return itemRepository.findById(id).orElse(null);
    }

    @Transactional
    public void edit(int id, Item updatedItem){
        Item item = itemRepository.findById(id).orElse(null);
        item.setItemName(updatedItem.getItemName());
        item.setItemDescription(updatedItem.getItemDescription());
        item.setPrice(updatedItem.getPrice());

        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<Item> findByShopAndCategory(int shopId, int categoryId){
        return itemRepository.findByShopAndCategory(shopRepository.findById(shopId).orElse(null), categoryRepository.findById(categoryId).orElse(null));
    }
}
