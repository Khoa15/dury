package com.example.apidury.service;

import com.example.apidury.model.Category;
import com.example.apidury.model.Note;
import com.example.apidury.repository.CategoryRepository;
import com.example.apidury.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    public List<Category> loadAll(){
        return categoryRepository.findAll();
    }

    public List<Category> getByUserId(String id) {
        Criteria criteria = Criteria.where("user.id").is(id);
        Query query = new Query(criteria);
        List<Category> categories = mongoTemplate.find(query, Category.class);
        return categories;
    }

    public Optional<Category> get(String id){
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category){
        Optional<Category> categorydb = categoryRepository.findById(category.getId());

        if(categorydb.isPresent()){
            Category categoryUpdate = categorydb.get();
            categoryUpdate.setName(category.getName());
            categoryUpdate.setColor(category.getColor());
            categoryRepository.save(categoryUpdate);
            return categoryUpdate;
        }else{
            throw new RuntimeException("Category not found!");
        }
    }


    public void deleteCategory(String id){
            Category category = categoryRepository.findById(id).orElse(null);
            Criteria criteria = Criteria.where("category.id").is(category.getId());
            Query query = new Query(criteria);
            List<Note> notes = mongoTemplate.find(query, Note.class);
            if(category != null){
                notes.forEach(noteRepository::delete);
                categoryRepository.deleteById(id);
            }
    }
}
