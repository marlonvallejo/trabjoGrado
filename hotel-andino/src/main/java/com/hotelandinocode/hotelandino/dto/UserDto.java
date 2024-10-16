package com.hotelandinocode.hotelandino.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserDto {
    private Long id;
    private String name;
    private String lastname;
    private String username;
    private String email;
    @NotBlank
    private String password;
    private Set<String> roles = new HashSet<>();
}
