package pawkar.backend.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import pawkar.backend.enums.ERole;
import pawkar.backend.entity.Role;
import pawkar.backend.entity.User;
import pawkar.backend.repositories.RoleRepository;
import pawkar.backend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import pawkar.backend.exception.BadRequestException;
import pawkar.backend.request.LoginRequest;
import pawkar.backend.request.SignupRequest;
import pawkar.backend.response.ApiResponseStandard;
import pawkar.backend.response.JwtResponse;
import pawkar.backend.security.jwt.JwtUtils;
import pawkar.backend.security.services.UserDetailsImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponseStandard<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);
            
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();        
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            JwtResponse jwtResponse = new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles,
                    refreshToken
            );

            return ResponseEntity.ok(ApiResponseStandard.success(
                    jwtResponse,
                    "Usuario autenticado exitosamente"
            ));
        } catch (Exception e) {
            throw new BadRequestException("Nombre de usuario o contraseña inválidos");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseStandard<?>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponseStandard.error(
                            "¡El nombre de usuario ya está en uso!",
                            "/auth/signup",
                            "Solicitud incorrecta",
                            HttpStatus.BAD_REQUEST.value()
                    ));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponseStandard.error(
                            "¡El correo electrónico ya está en uso!",
                            "/auth/signup",
                            "Solicitud incorrecta",
                            HttpStatus.BAD_REQUEST.value()
                    ));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(ApiResponseStandard.success(
                null,
                "¡Usuario registrado exitosamente!"
        ));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<ApiResponseStandard<JwtResponse>> refreshtoken() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);
            
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();        
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            JwtResponse jwtResponse = new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles,
                    refreshToken
            );

            return ResponseEntity.ok(ApiResponseStandard.success(
                    jwtResponse,
                    "Token refreshed successfully"
            ));
        } catch (Exception e) {
            throw new BadRequestException("Failed to refresh token");
        }
    }
}
