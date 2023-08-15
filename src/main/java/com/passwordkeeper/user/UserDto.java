package com.passwordkeeper.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {

    private String id;
    private String login;
    private Integer passwordsCount;
    private UserAccountType accountType;
}
