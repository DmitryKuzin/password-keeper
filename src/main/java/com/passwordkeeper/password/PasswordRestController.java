package com.passwordkeeper.password;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PasswordRestController {

    @Autowired
    private PasswordService passwordService;

    @GetMapping("passwords/{userId}/all")
    public List<PasswordDto> getPasswords(@PathVariable String userId) {
        return passwordService.getAllPasswordsByUserId(userId);
    }

    @GetMapping("passwords/{userId}")
    public String getPasswordByWebsiteName(@RequestParam("website_name") String websiteName, @PathVariable String userId) {
        String result = passwordService.getPasswordByWebsiteNameAndUserId(websiteName, userId);
        return result != null ? result : "ERROR";
    }

    @PostMapping("passwords")
    public String savePassword(@RequestBody PasswordDto passwordDto) {
        return passwordService.savePassword(passwordDto) ? "SUCCESS" : "ERROR";
    }

    @DeleteMapping("passwords/{userId}/all")
    public String deletePasswords(@PathVariable String userId) {
        return passwordService.deleteAllByUserId(userId) ? "SUCCESS" : "ERROR";
    }

    @DeleteMapping("passwords/{userId}")
    public String deleteWebsiteByName(@RequestParam("website_name") String websiteName, @PathVariable String userId) {
        return passwordService.deleteByWebsiteNameAndUserId(websiteName, userId) ? "SUCCESS" : "ERROR";
    }
}
