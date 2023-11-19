package org.example.data.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
}
