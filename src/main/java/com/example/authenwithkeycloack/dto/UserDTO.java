package com.example.authenwithkeycloack.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private int statusCode;
    private String status;
    private String role;
}
