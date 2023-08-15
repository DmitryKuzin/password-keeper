package com.passwordkeeper.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("users/{userId}")
    public UserDto getUserById(@PathVariable String userId) {
        return userService.findById(userId);
    }

    @PostMapping("users")
    public String saveUser(@RequestBody UserRequestDto requestDto) {
        UserDto result = userService.findByLogin(requestDto.getLogin());
        if (result == null) {
            UserDto saved = userService.create(requestDto);
            return String.valueOf(saved.getId());
        } else {
            log.error("User with login " + requestDto.getLogin() + " already exists in database");
            return "ERROR";
        }
    }
}
