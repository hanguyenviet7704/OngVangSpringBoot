package org.example.shoppefood.dto.securiry;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    // Getters and setters
}
