package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.model.User;
import cz.cvut.fel.pm2.timely_be.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>());
    }

//    public User createUser(User user) {
//        // Check if user already exists
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            throw new RuntimeException("User already exists with email: " + user.getEmail());
//        }
//
//        // Encode password
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        // Save user
//        return userRepository.save(user);
//    }

    @Transactional
    public void createUsers(List<User> users) {
        // Encode passwords for all users
        users.forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
        
        // Filter out users that already exist
        List<String> existingEmails = userRepository.findByEmailIn(
            users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList())
        ).stream()
            .map(User::getEmail)
            .toList();

        List<User> newUsers = users.stream()
            .filter(user -> !existingEmails.contains(user.getEmail()))
            .collect(Collectors.toList());

        // Batch save new users
        if (!newUsers.isEmpty()) {
            userRepository.saveAll(newUsers);
        }
    }
} 