package pawkar.backend.service;

import org.springframework.stereotype.Service;
import pawkar.backend.entity.User;
import pawkar.backend.exception.ResourceNotFoundException;
import pawkar.backend.repository.UserRepository;
import pawkar.backend.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return UserResponse.fromUser(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));
        return UserResponse.fromUser(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }
}