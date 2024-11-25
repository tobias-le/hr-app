package cz.cvut.fel.pm2.timely_be.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String email;
    private String password;
} 