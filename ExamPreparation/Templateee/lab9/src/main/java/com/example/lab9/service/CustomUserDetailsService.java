// package com.example.lab9.service;

// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import
// org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// @Service
// public class CustomUserDetailsService implements UserDetailsService {

// private final IUserRepository userRepository;

// public CustomUserDetailsService(IUserRepository userRepository) {
// this.userRepository = userRepository;
// }

// @Override
// public UserDetails loadUserByUsername(String username) throws
// UsernameNotFoundException {
// AppUser user = userRepository.findByUsername(username)
// .orElseThrow(() -> new UsernameNotFoundException("User not found: " +
// username));
// return User.builder()
// .username(user.getUsername())
// .password(user.getPassword())
// .roles(user.getRoles().toArray(new String[0]))
// .build();
// }
// }
