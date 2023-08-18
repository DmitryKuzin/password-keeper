package com.passwordkeeper.password;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("passwords")
public class PasswordRestController {

    @Autowired
    private PasswordService passwordService;

    @GetMapping("{userId}/all")
    public ResponseEntity<List<PasswordDto>> getPasswords(@PathVariable String userId) {
        return ResponseEntity.ok(passwordService.getAllPasswordsByUserId(userId));
    }

    @GetMapping("{userId}")
    public ResponseEntity<String> getPasswordByWebsiteName(@RequestParam("website_name") String websiteName, @PathVariable String userId) {
        String result = passwordService.getPasswordByWebsiteNameAndUserId(websiteName, userId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> savePassword(@RequestBody PasswordDto passwordDto) {
        return passwordService.savePassword(passwordDto) ? ResponseEntity.ok("\"status\" : \"OK\"") : ResponseEntity.internalServerError().body("ERROR");
    }

    @DeleteMapping("{userId}/all")
    public ResponseEntity<String> deletePasswords(@PathVariable String userId) {
        return passwordService.deleteAllByUserId(userId) ? ResponseEntity.ok("\"status\" : \"OK\"") : ResponseEntity.internalServerError().body("ERROR");
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<String> deleteWebsiteByName(@RequestParam("website_name") String websiteName, @PathVariable String userId) {
        return passwordService.deleteByWebsiteNameAndUserId(websiteName, userId) ? ResponseEntity.ok("\"status\" : \"OK\"") : ResponseEntity.internalServerError().body("ERROR");
    }
}
