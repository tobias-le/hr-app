package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.model.User;
import cz.cvut.fel.pm2.timely_be.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void loadUserByUsername_WhenUserExists_ReturnsUserDetails() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encoded_password");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("nonexistent@example.com"));
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void createUsers_WhenAllUsersAreNew_SavesAll() {
        // Arrange
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");

        List<User> users = Arrays.asList(user1, user2);
        
        when(passwordEncoder.encode("password1")).thenReturn("encoded_password1");
        when(passwordEncoder.encode("password2")).thenReturn("encoded_password2");
        when(userRepository.findByEmailIn(anyList())).thenReturn(List.of());
        when(userRepository.saveAll(anyList())).thenReturn(users);

        // Act
        userService.createUsers(users);

        // Assert
        verify(passwordEncoder).encode("password1");
        verify(passwordEncoder).encode("password2");
        verify(userRepository).findByEmailIn(Arrays.asList("user1@example.com", "user2@example.com"));
        verify(userRepository).saveAll(users);
    }

    @Test
    void createUsers_WhenSomeUsersExist_SavesOnlyNewUsers() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword("password1");

        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setPassword("password2");

        List<User> usersToCreate = Arrays.asList(existingUser, newUser);
        
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(userRepository.findByEmailIn(anyList()))
                .thenReturn(List.of(existingUser));

        // Act
        userService.createUsers(usersToCreate);

        // Assert
        verify(userRepository).findByEmailIn(Arrays.asList("existing@example.com", "new@example.com"));
        verify(userRepository).saveAll(argThat(savedUsers -> 
                StreamSupport.stream(savedUsers.spliterator(), false).count() == 1 && 
                StreamSupport.stream(savedUsers.spliterator(), false).findFirst().get().getEmail().equals("new@example.com")));
    }

    @Test
    void createUsers_WhenAllUsersExist_SavesNone() {
        // Arrange
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");

        List<User> users = Arrays.asList(user1, user2);
        
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(userRepository.findByEmailIn(anyList()))
                .thenReturn(Arrays.asList(user1, user2));

        // Act
        userService.createUsers(users);

        // Assert
        verify(userRepository).findByEmailIn(Arrays.asList("user1@example.com", "user2@example.com"));
        verify(userRepository, never()).saveAll(anyList());
    }
} 