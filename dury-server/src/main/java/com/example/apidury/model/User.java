package com.example.apidury.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private boolean isBiometric;
    @DBRef
    List<Category> listCategories;

    @DBRef
    private Set<Role> roles = new HashSet<>();
    public User(){}

    public User(String uid, String username) {
        this.id = uid;
        this.username = username;
        this.isBiometric = false;
    }


    public User(String username) {
        this.username = username;
        this.isBiometric = false;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBiometric() {
        return isBiometric;
    }

    public void setBiometric(boolean biometric) {
        isBiometric = biometric;
    }
}
