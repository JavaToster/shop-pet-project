package org.example.services;

import org.example.models.Category;
import org.example.models.Shop;
import org.example.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private CategoryRepository categoryRepository;
    private ShopService shopService;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }

    public List<Category> findAllCategories(){
        return categoryRepository.findAll();
    }

    public List<Category> findCategoriesByShopId(int id){
        return categoryRepository.findByShops(shopService.findById(id));
    }

    public Category findById(int id){
        return categoryRepository.findById(id).orElse(null);
    }
}
