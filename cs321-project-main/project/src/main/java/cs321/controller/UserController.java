package cs321.project.controller;

import cs321.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    // Inner interface for UserRepository
    private interface UserRepository {
        Optional<User> findByUsername(String username);
    }

    // Mock implementation of UserRepository
    private static class MockUserRepository implements UserRepository {
        @Override
        public Optional<User> findByUsername(String username) {
            // Implement the logic for finding a user by username
            // This is just a mock implementation for demonstration purposes
            // Replace it with your actual implementation
            return null;
        }
    }

    private UserRepository userRepository;

    public UserController() {
        this.userRepository = new MockUserRepository();
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser == null) {
            System.out.println("h\n\n\n\n\n\n\n");
        }

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            if (existingUser.getPassword().equals(user.getPassword())) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}