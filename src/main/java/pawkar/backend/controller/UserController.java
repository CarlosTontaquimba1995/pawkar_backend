package pawkar.backend.controller;

import org.springframework.web.bind.annotation.*;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.UserResponse;
import pawkar.backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ApiResponseStandard<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ApiResponseStandard.success(
            user,
            "Usuario obtenido exitosamente"
        );
    }

    @GetMapping("/username/{username}")
    public ApiResponseStandard<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse user = userService.getUserByUsername(username);
        return ApiResponseStandard.success(
            user,
            "Usuario obtenido exitosamente"
        );
    }

    @GetMapping
    public ApiResponseStandard<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ApiResponseStandard.success(
                users,
            "Usuarios obtenidos exitosamente"
        );
    }
}