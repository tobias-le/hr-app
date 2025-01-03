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
                
        // Check if the associated employee is deleted
        if (user.getEmployee() != null && user.getEmployee().isDeleted()) {
            throw new UsernameNotFoundException("User account has been deleted");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>());
    }

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