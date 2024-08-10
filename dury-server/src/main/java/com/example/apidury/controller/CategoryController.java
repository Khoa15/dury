package com.example.apidury.controller;

import com.example.apidury.model.Category;
import com.example.apidury.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> loadAll(){
        return categoryService.loadAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> get(String id){
        Category category = categoryService.get(id).orElseThrow(() -> new RuntimeException("Not found"));
        return ResponseEntity.ok().body(category);
    }
    @GetMapping("/u/{id}")
    public List<Category> getByUid(@PathVariable("id") String id){
        return  categoryService.getByUserId(id);
    }

    @PostMapping
    public ResponseEntity<Category> insertCategory(@RequestBody Category category){
        try{
            return new ResponseEntity<Category>(categoryService.createCategory(category), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Category> updateCategory(@RequestBody Category category){
        try{
            return new ResponseEntity<Category>(categoryService.updateCategory(category), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteCategory(@PathVariable(value = "id") String id){
        categoryService.deleteCategory(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
