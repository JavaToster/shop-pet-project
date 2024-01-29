package org.example.controllers;


import org.example.models.Category;
import org.example.models.Comment;
import org.example.models.Item;
import org.example.models.Shop;
import org.example.services.CategoryService;
import org.example.services.CommentService;
import org.example.services.ItemService;
import org.example.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private ItemService itemService;
    private CategoryService categoryService;
    private ShopService shopService;
    private CommentService commentService;

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping()
    public String mainWindow(Model model) {
        model.addAttribute("items", itemService.findAllItems());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("category", new Category());
        model.addAttribute("item", new Item());

        return "main";
    }

    @GetMapping("/{id}/add-item")
    public String addItem(Model model, @PathVariable("id") int id) {
        model.addAttribute("shop_id", id);
        model.addAttribute("item", new Item());
        return "newItem";
    }

    @PostMapping("/{id}/add-item-step-1")
    public String addItemPostStepOne(@PathVariable("id") int id, @ModelAttribute("item") Item item, Model model) {
        item.setId(0);

        int indexItemInCash = itemService.addToCashStep1(item);
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("item_id", indexItemInCash);
        model.addAttribute("shop_id", id);

        System.out.println("good1");

        return "newItemStep2";
    }

    @PostMapping("/{id}/add-item-step-2")
    public String addItemPostStepTwo(@ModelAttribute("category") Category category, Model model,
                                     @RequestParam("item-id") int itemId, @PathVariable("id") int shopId) {
        itemService.addToCashStep2(itemId, categoryService.findById(category.getId()));
        model.addAttribute("shop_id", shopId);
        model.addAttribute("item_id", itemId);

        itemService.add(shopId, itemId);
        shopService.addCategory(shopService.findById(shopId), categoryService.findById(category.getId()));

        return "redirect:/shop";
    }

    @GetMapping("/new")
    public String newShop(Model model) {
        model.addAttribute("shop", new Shop());

        return "newShop";
    }

    @PatchMapping("/new")
    public String newShopPatch(@ModelAttribute("shop") Shop shop) {
        shopService.add(shop);

        return "redirect:/shop/";
    }

    @GetMapping("/{id}")
    public String showShop(@PathVariable("id") int id, Model model) {
        model.addAttribute("shop", shopService.findById(id));
        model.addAttribute("categories", categoryService.findCategoriesByShopId(id));
        model.addAttribute("items", itemService.findItemByShop(id));
        model.addAttribute("category", new Category());

        return "shop";
    }

    @PostMapping("/redirect-to-categories")
    public String redirectToCategories(@ModelAttribute("category") Category category) {
        return "redirect:/shop/category/" + category.getId();
    }

    @GetMapping("/category/{categoryId}")
    public String showCategory(@PathVariable("categoryId") int categoryId, Model model) {
        model.addAttribute("items", itemService.findByCategory(categoryId));
        model.addAttribute("item", new Item());
        model.addAttribute("category", categoryService.findById(categoryId));

        return "showCategory";
    }

    @PostMapping("/search")
    public String search(@RequestParam("search-text") String searchText, Model model) {
        model.addAttribute("items", itemService.findItemLikeSearchText(searchText));
        model.addAttribute("item", new Item());
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("text", searchText);


        return "search";
    }

    @GetMapping("/item/{id}")
    public String showItem(@PathVariable("id") int id, Model model) {
        model.addAttribute("item", itemService.findById(id));
        model.addAttribute("comments", commentService.findByItem(id));
        model.addAttribute("comment", new Comment());

        return "showItem";
    }

    @GetMapping("/item/{id}/edit")
    public String editItem(@PathVariable("id") int id, Model model){
        Item item = itemService.findById(id);
        model.addAttribute("item", item);
        model.addAttribute("itemId", item.getId());
        model.addAttribute("shop", item.getShop());

        return "editItem";
    }

    @PostMapping("/item/{id}/edit")
    public String editItemPost(@PathVariable("id") int id, @ModelAttribute("item") Item item){
        itemService.edit(id, item);

        return "redirect:/shop/item/"+id;
    }

    @GetMapping("/{shopId}/category/{categoryId}")
    public String showCategoryOfShop(@PathVariable("shopId") int shopId, @PathVariable("categoryId") int categoryId,
                                     Model model){
        model.addAttribute("items", itemService.findByShopAndCategory(shopId, categoryId));
        model.addAttribute("item", new Item());
        model.addAttribute("category", categoryService.findById(categoryId));

        return "showCategory";
    }
}
