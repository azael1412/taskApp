package com.azael.taskapp.services.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.azael.taskapp.exceptions.InvalidAuthException;
import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.persistence.dto.request.auth.LoginRequestDto;
import com.azael.taskapp.persistence.dto.request.auth.RefreshTokenRequestDto;
import com.azael.taskapp.persistence.dto.request.auth.RegisterRequestDto;
import com.azael.taskapp.persistence.dto.response.auth.LoginResponseDto;
import com.azael.taskapp.persistence.dto.response.user.UserResponseDto;
import com.azael.taskapp.persistence.repositories.UserRepository;
import com.azael.taskapp.persistence.repositories.RevokedTokenRepository;
import com.azael.taskapp.persistence.repositories.RoleRepository;
import com.azael.taskapp.services.AuthService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.azael.taskapp.persistence.entities.User;
import com.azael.taskapp.persistence.mappers.UserMapper;
import com.azael.taskapp.persistence.entities.RefreshToken;
import com.azael.taskapp.persistence.entities.RevokedToken;
import com.azael.taskapp.persistence.entities.Role;

@Component
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;
    @Autowired
    private RevokedTokenRepository revokedTokenRepository;
    @Value("${api.security.token.refresh-token-expiration}")
    private int REFRESH_TOKEN_EXPIRATION;
    //private static final int REFRESH_TOKEN_EXPIRATION = 60 * 24 * 7; // 7 días
    @Transactional
    @Override
    public LoginResponseDto login(LoginRequestDto request) throws BadCredentialsException, InvalidAuthException {
            Optional<User> user = userRepository.findByUsernameAndIsActive(request.username(), true);
            if (user.isEmpty()) {
                throw new InvalidAuthException("Invalid Credentials");
            }
           // Verificar si el usuario está activo
            // if (!user.get().isActive()) {
            //     throw new InvalidAuthException("You are deactivated");
            // }
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
    
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = (User) userDetails;
            UserResponseDto userDto = UserMapper.toDTO(currentUser);
            // Generar tokens
            // String accessToken = jwtService.generateToken(userDetails);
            Map<String, String> accessTokenDetails = jwtService.generateToken(userDetails);
        
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(currentUser, REFRESH_TOKEN_EXPIRATION); // 7 días
            return new LoginResponseDto((String) accessTokenDetails.get("token"), "Bearer", refreshToken.getToken(), (String) accessTokenDetails.get("expiresAt"), userDto );
    }
    @Transactional
    @Override
    public LoginResponseDto refreshToken(String authHeader, RefreshTokenRequestDto request) throws ServiceLogicException {
            String token = authHeader.substring(7); // Extraer el token después de "Bearer "
            this.revokeToken(token); // Reutilizamos el método para revocar el token

            RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.refreshToken());
            // Obtener el usuario asociado al refresh token
            User user = refreshToken.getUser();
            UserResponseDto userDto = UserMapper.toDTO(user);
        
            // Generar un nuevo access token
            // String newAccessToken = jwtService.generateToken(user);
            // Long expiresIn = jwtService.getExpirationTime();
            Map<String, String> accessTokenDetails = jwtService.generateToken(user);
            //RefreshToken currentRefreshToken = refreshTokenService.createRefreshToken(user, 60 * 24 * 7); // 7 días
            return new LoginResponseDto((String) accessTokenDetails.get("token"), "Bearer", refreshToken.getToken(),  (String) accessTokenDetails.get("expiresAt"), userDto );
    }

    @Transactional
    @Override
    public UserResponseDto register(RegisterRequestDto request) {
            // Mapa para almacenar errores de validación
            // Map<String, Map<String, String>> errors = new HashMap<>();
    
            // // Verificar si ya existe un usuario con el mismo correo
            // Optional<User> existingUserByEmail = userRepository.findByEmail(request.email());
            // if (existingUserByEmail.isPresent()) {
            //     log.info("User already registered: " + request.email());
            //     Map<String, String> emailError = new HashMap<>();
            //     emailError.put("message", "Registration failed: User already exists with email " + request.email());
            //     errors.put("email", emailError);
            // }
    
            // // Verificar si ya existe un usuario con el mismo nombre de usuario
            // Optional<User> existingUserByUsername = userRepository.findByUsername(request.username());
            // if (existingUserByUsername.isPresent()) {
            //     log.info("User already registered: " + request.username());
            //     Map<String, String> usernameError = new HashMap<>();
            //     usernameError.put("message", "Registration failed: User already exists with username " + request.username());
            //     errors.put("username", usernameError);
            // }

            // if (request.password().length() < 6 || request.password().length() > 16) {
            //     Map<String, String> passwordError = new HashMap<>();
            //     passwordError.put("message", "Password must be between 6 and 16 characters");
            //     errors.put("password", passwordError);
            // }
    
            // if (!errors.isEmpty()) {
            //     throw new DataInvalidException("Validation failed", errors);
            // }
    
            // Recuperar el rol del repositorio
            Role role = roleRepository.findById(2L)
            .orElseThrow(() -> new ServiceLogicException("Role not found"));
    
            User newUser = new User(
                    request.name(),
                    request.username(),
                    request.email(),
                    request.phone(),
                    passwordEncoder.encode(request.password()),
                    true,
                    role // Aquí ya pasamos el objeto Role directamente
            );
            // Guardar el usuario en la base de datos
            User currentUser = userRepository.save(newUser);
            return UserMapper.toDTO(currentUser);
    }
    @Override
    public User me() throws ServiceLogicException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Validar que el usuario esté autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ServiceLogicException("No authenticated user");
        }
    
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
    
        // Verificar si el usuario existe y retornar el DTO correspondiente
        return user.orElseThrow(() -> new ServiceLogicException("User not found"));
    }
    @Transactional
    @Override
    public void logout(String authHeader) {
        // Extraer el token
        String token = authHeader.substring(7);
    
        // Revocar el token
        revokeToken(token);
    
        // Obtener el usuario autenticado y eliminar el refresh token asociado
        User user = this.me();
        Optional.ofNullable(user)
                .ifPresentOrElse(
                        u -> refreshTokenService.deleteRefreshToken(u.getId()),
                        () -> { throw new IllegalStateException("User not found"); }
                );
    }
    

    private void revokeToken(String token) {
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(token);
        revokedToken.setRevokedAt(LocalDateTime.now());
        revokedTokenRepository.save(revokedToken);
    }
}
