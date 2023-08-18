package com.passwordkeeper.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        UserDto user = userService.findById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody UserRequestDto requestDto) {
        UserDto result = userService.findByLogin(requestDto.getLogin());
        if (result == null) {
            UserDto saved = userService.create(requestDto);
            return ResponseEntity.ok(String.valueOf(saved.getId()));
        } else {
            String errorMessage = String.format("User with login \"%s\" already exists in database!", requestDto.getLogin());
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
}
