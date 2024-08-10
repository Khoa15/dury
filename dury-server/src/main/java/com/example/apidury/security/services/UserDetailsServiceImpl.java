package com.example.apidury.security.services;


import com.example.apidury.model.User;
import com.example.apidury.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

  public UserDetailsImpl  loadUserByUid(String uid){
    User user = userRepository.findById(uid)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with uid: "+uid));
    return UserDetailsImpl.build(user);
  }

}
