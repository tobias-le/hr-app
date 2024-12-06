package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.AuthRequestDto;
import cz.cvut.fel.pm2.timely_be.dto.AuthResponseDto;
import cz.cvut.fel.pm2.timely_be.dto.EmployeeDto;
import cz.cvut.fel.pm2.timely_be.dto.RefreshTokenRequestDto;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.security.JwtUtil;
import cz.cvut.fel.pm2.timely_be.service.EmployeeService;
import cz.cvut.fel.pm2.timely_be.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final EmployeeService employeeService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil, EmployeeService employeeService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        final UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        final String accessToken = jwtUtil.generateToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        EmployeeDto employeeDto = MapperUtils.toEmployeeDto(
            employeeService.getEmployeeByEmail(authRequest.getEmail())
        );

        return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken, employeeDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto refreshRequest) {
        try {
            // Validate refresh token
            if (!jwtUtil.isRefreshToken(refreshRequest.getRefreshToken())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Extract username from refresh token
            final String username = jwtUtil.extractUsername(refreshRequest.getRefreshToken());
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Load user details
            final UserDetails userDetails = userService.loadUserByUsername(username);
            
            // Validate refresh token
            if (!jwtUtil.validateToken(refreshRequest.getRefreshToken(), userDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Generate new tokens
            final String newAccessToken = jwtUtil.generateToken(userDetails);
            final String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

            // Get employee details
            EmployeeDto employeeDto = MapperUtils.toEmployeeDto(
                employeeService.getEmployeeByEmail(username)
            );

            return ResponseEntity.ok(new AuthResponseDto(newAccessToken, newRefreshToken, employeeDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
} 