package com.example.apidury.service;

import com.example.apidury.model.Note;
import com.example.apidury.model.User;
import com.example.apidury.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> loadAll() {
        return userRepository.findAll();
    }

    public Optional<User> get(String userId){
        return userRepository.findById(userId);
    }

    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (!existingUser.isPresent()) {
            userRepository.save(user);
        }

        return user;
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }

    public User updateUser(User user) {
        Optional<User> userdb = this.userRepository.findById(user.getId());

        if(userdb.isPresent()){
            User userUpdate = userdb.get();
            userUpdate.setId(user.getId());
            userUpdate.setUsername(user.getUsername());
            userUpdate.setRoles(user.getRoles());
            userUpdate.setBiometric(!user.isBiometric());
            userRepository.save(userUpdate);
            return userUpdate;
        }
        else{
            throw new RuntimeException("Note not found!");
        }
    }
}
