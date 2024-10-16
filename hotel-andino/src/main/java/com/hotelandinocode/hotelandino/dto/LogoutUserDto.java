package com.hotelandinocode.hotelandino.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class LogoutUserDto {
    private String userNameTo;
    private String userNameFrom;
}
